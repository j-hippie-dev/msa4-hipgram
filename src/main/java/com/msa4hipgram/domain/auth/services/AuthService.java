package com.msa4hipgram.domain.auth.services;

import com.msa4hipgram.domain.auth.mapper.AuthMapper;
import com.msa4hipgram.domain.auth.requests.LoginReq;
import com.msa4hipgram.domain.auth.responses.AuthRes;
import com.msa4hipgram.domain.user.entities.User;
import com.msa4hipgram.domain.user.mapper.UserMapper;
import com.msa4hipgram.domain.user.responses.UserRes;
import com.msa4hipgram.global.errors.custom.NotRegisteredException;
import com.msa4hipgram.global.security.cookie.CookieManager;
import com.msa4hipgram.global.security.jwt.JwtConfig;
import com.msa4hipgram.global.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;
    private final AuthMapper authMapper;
    private final CookieManager cookieManager;
    private final JwtConfig jwtConfig;

    public AuthRes login(HttpServletResponse response, LoginReq loginReq) {
        // 유저 정보 획득
        User user = userMapper.findByEmail(loginReq.email());

        // 유저 가입 여부 확인
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        // 비밀번호 체크

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
}
