package com.msa4hipgram.global.security.jwt;

import com.msa4hipgram.domain.user.entities.UserMybatis;
import com.msa4hipgram.global.errors.custom.InvalidTokenException;
import com.msa4hipgram.global.security.cookie.CookieManager;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Component // 클래스 레벨에 붙이는 어노테이션
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final CookieManager cookieManager;

    // 생성자를 커스텀해서 만들어야 함. -> @RequiredArgsConstructor 쓸 필요 X
    public JwtProvider(JwtConfig jwtConfig, CookieManager cookieManager) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
        this.cookieManager = cookieManager;
    }

    private String generateToken(UserMybatis user, long ttl) {
        Date now = new Date();

        return Jwts.builder()
                .header() // 헤더 셋팅하겠다.
                .type(jwtConfig.type()) // 토큰 유형 설정
                .and() // 추가 연결
                .subject(String.valueOf(user.getId())) // subject: 유저를 특정하는 id셋팅에 (유저를 인식할 수 있는 id) 주로 사용 // String 타입만 올 수 있음. -> 형변환 해줘야함
                .issuer(jwtConfig.issuer()) // 토큰 발급자
                .issuedAt(now) // 토큰 발급 시간 // 알아서 타임스탬프로 잘 저장함
                .expiration(new Date(now.getTime() + ttl)) // 토큰 만료 시간
                .claim("role", user.getRole()) // private claim 설정
                .signWith(this.secretKey) // 시그니처 작성
                .compact();
    }

    public String generateAccessToken(UserMybatis user) {
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    public String generateRefreshToken(UserMybatis user) {
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }

    // 쿠키에서 리프레쉬 토큰 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return cookieManager.getCookie(request, jwtConfig.refreshTokenCookieName())
                .map(Cookie::getValue);
    }

    /**
     * 헤더에서 베어러토큰(엑세스토큰) 추출
     * @param request 리퀘스트
     * @return Optional 엑세스 토큰
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(jwtConfig.headerKey());

        if(bearerToken == null || !bearerToken.startsWith(jwtConfig.scheme())) {
            return Optional.empty();
        }

        return Optional.of(bearerToken.substring(jwtConfig.scheme().length()).trim());
    }

    // 토큰 검증 및 클레임 추출
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(this.secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("토큰이 만료됐습니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("서명이 위조된 토큰입니다.");
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("토큰 형식이 올바르지 않습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("토큰 검증에 실패했습니다.");
        }
    }
}