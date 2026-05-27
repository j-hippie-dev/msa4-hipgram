package com.msa4hipgram.domain.auth.controllers;

import com.msa4hipgram.domain.auth.requests.LoginReq;
import com.msa4hipgram.domain.auth.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @Valid @RequestBody LoginReq loginReq
        , HttpServletResponse response // 쿠키 저장할 때 사용할 객체
    ) {
        authService.login(loginReq);

        return ResponseEntity.status(200).body("test");
    }
}
