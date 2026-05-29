package com.msa4hipgram.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

// 환경설정 파일에 prefix 경로에 있는 값들을 가져오겠다.
@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfig(
    boolean secure,
    String issuer,
    String type,
    int accessTokenExpiry,
    int refreshTokenExpiry,
    String refreshTokenCookieName,
    int refreshTokenCookieExpiry,
    String secret,
    String headerKey,
    String scheme,
    String reissUri
) {
}
