package com.msa4hipgram.global.security.jwt;

import com.msa4hipgram.domain.user.entities.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component // 클래스 레벨에 붙이는 어노테이션
public class JwtProvider {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    // 생성자를 커스텀해서 만들어야 함. -> @RequiredArgsConstructor 쓸 필요 X
    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secret()));
    }

    private String generateToken(User user, long ttl) {
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
                .signWith(secretKey) // 시그니처 작성
                .compact();
    }

    public String generateAccessToken(User user) {
        return this.generateToken(user, jwtConfig.accessTokenExpiry());
    }

    public String generateRefreshToken(User user) {
        return this.generateToken(user, jwtConfig.refreshTokenExpiry());
    }
}
