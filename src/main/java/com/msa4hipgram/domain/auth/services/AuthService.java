package com.msa4hipgram.domain.auth.services;

import com.msa4hipgram.domain.auth.mapper.AuthMapper;
import com.msa4hipgram.domain.auth.requests.LoginReq;
import com.msa4hipgram.domain.auth.requests.RegistrationReq;
import com.msa4hipgram.domain.auth.responses.AuthRes;
import com.msa4hipgram.domain.user.entities.User;
import com.msa4hipgram.domain.user.mapper.UserMapper;
import com.msa4hipgram.domain.user.responses.UserRes;
import com.msa4hipgram.global.errors.custom.DuplicatedRecordException;
import com.msa4hipgram.global.errors.custom.InvalidTokenException;
import com.msa4hipgram.global.errors.custom.NotRegisteredException;
import com.msa4hipgram.global.security.constant.ProviderPolicy;
import com.msa4hipgram.global.security.constant.RolePolicy;
import com.msa4hipgram.global.security.cookie.CookieManager;
import com.msa4hipgram.global.security.jwt.JwtConfig;
import com.msa4hipgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    public AuthRes login(HttpServletResponse response, LoginReq loginReq) {
        // 유저 정보 획득
        User user = userMapper.findByEmail(loginReq.email());

        // 유저 가입 여부 확인
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        // 비밀번호 체크
        if(!passwordEncoder.matches(loginReq.password(), user.getPassword())) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        return this.generateAuthentication(response, user);
    }

    @Transactional(rollbackFor = Exception.class)
    public AuthRes reissue(HttpServletRequest request, HttpServletResponse response) {
        // 리프레시 토큰 획득
        // Optional: null이 올 수 있는 값을 감싸는 Wrapper 클래스
        Optional<String> refreshTokenOptional = jwtProvider.extractRefreshToken(request);
        if(refreshTokenOptional.isEmpty()) {
            throw new InvalidTokenException("토큰이 없습니다.");
        }
        String extractRefreshToken = refreshTokenOptional.get();

        long id = Long.parseLong(jwtProvider.extractClaims(extractRefreshToken).getSubject());

        // 유저 정보 획득
        User user = userMapper.findByPk(id);

        // 유저 가입 여부 확인 및 비로그인 상태 확인
        if(user == null || user.getRefreshToken() == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        // 리프레쉬 토큰 비교
        if(!user.getRefreshToken().equals(extractRefreshToken)) {
            throw new InvalidTokenException("토큰이 일치하지 않습니다.");
        }

        return this.generateAuthentication(response, user);
    }

    /**
     * 엑세스 토큰 및 리프레쉬 토큰 생성 후, 리프레쉬 토큰 DB & Cookie에 저장, AuthRes로 반환
     * @param response
     * @param user 유저 Entity
     * @return AuthRes
     */
    private AuthRes generateAuthentication(HttpServletResponse response, User user) {
        // 토큰 생성
        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        // 리프레쉬 토큰 DB에 저장 // 우리는 DB에 저장할거임.
        authMapper.updateRefreshToken(user.getId(), newRefreshToken);

        // 리프레쉬 토큰 Cookie에 저장
        cookieManager.setCookie(
            response
            , jwtConfig.refreshTokenCookieName()
            , newRefreshToken
            , jwtConfig.refreshTokenCookieExpiry()
            , jwtConfig.reissUri()
        );

        // 리턴
        return AuthRes.builder()
            .accessToken(newAccessToken)
            .user(
                UserRes.builder()
                    .email(user.getEmail())
                    .nick(user.getNick())
                    .role(user.getRole())
                    .profile(user.getProfile())
                    .createdAt(user.getCreatedAt())
                    .build()
            )
            .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public void logout(HttpServletResponse response, long id) {
        // 유저 정보 획득
        User user = userMapper.findByPk(id);

        if(user == null) {
            throw new InvalidTokenException("유효하지 않은 회원의 토큰입니다.");
        }

        // DB에 저장한 리프레쉬 토큰 파기
        authMapper.updateRefreshToken(id, null);

        // Cookie에 저장한 리프레쉬 토큰 파기
        cookieManager.setCookie(response, jwtConfig.refreshTokenCookieName(), null, 0, jwtConfig.reissUri());
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationReq registrationReq) {
        // 유저 정보 획득
        User user = userMapper.findByEmail(registrationReq.email());

        if(user != null) {
            throw new DuplicatedRecordException("이미 가입된 회원입니다.");
        }

        User newUser = new User();
        newUser.setEmail(registrationReq.email());
        newUser.setPassword(passwordEncoder.encode(registrationReq.password()));
        newUser.setNick(registrationReq.nick());
        newUser.setProfile(registrationReq.profile());
        newUser.setProvider(ProviderPolicy.NONE.getProvider());
        newUser.setRole(RolePolicy.NORMAL.getRole());
        authMapper.create(newUser);
    }
}
