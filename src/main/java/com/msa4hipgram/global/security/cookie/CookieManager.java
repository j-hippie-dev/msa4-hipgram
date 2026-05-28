package com.msa4hipgram.global.security.cookie;

import com.msa4hipgram.global.security.jwt.JwtConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CookieManager {
    private final JwtConfig jwtConfig;

    // Request Header에서 특정 쿠키를 획득 (Optional 반환)
    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        // 쿠키 존재 여부 확인
        if(request.getCookies() == null) {
            return Optional.empty(); // 데이터가 비어있는지 확인하는 메소드
        }

        // name과 일치하는 쿠키 획득하는 처리
        return Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst();
    }

    // 쿠키 생성 메소드
    public void setCookie(
            HttpServletResponse response
            , String name
            , String value
            , int maxAge
            , String path
    ) {
        Cookie cookie = new Cookie(name, value); // 해당 이름과 값으로 쿠키 인스턴스 생성
        cookie.setPath(path); // 쿠키를 사용할 path 설정
        cookie.setMaxAge(maxAge); // 쿠키 유효 시간 설정
        cookie.setHttpOnly(true); // HttpOnly 설정 (XSS 공격 방지 설정) // 자바스크립트 코드가 접근할 수 없음.
        cookie.setSecure(jwtConfig.secure()); // Secure 설정 (MITM 공격 방지) // 통신 중 공격자가 가로챔

        response.addCookie(cookie); // response에 쿠키 셋팅값 담아주는 메소드
    }
}
