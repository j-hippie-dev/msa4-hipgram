package com.msa4hipgram.domain.user.controllers;

import com.msa4hipgram.domain.auth.responses.AuthRes;
import com.msa4hipgram.domain.user.services.UserService;
import com.msa4hipgram.global.responses.GlobalRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<GlobalRes<AuthRes>> test() {
        // AuthRes result = userService.test();
        //
        // GlobalRes<AuthRes> globalRes = GlobalRes.<AuthRes>builder()
        //         .code("00")
        //         .message("정상 처리")
        //         .data(result)
        //         .build();
        //
        // return ResponseEntity.status(200).body(globalRes);
        // 한 번만 쓸거라서 변수에 담을 필요 없음.

        return ResponseEntity.status(200).body(
                GlobalRes.<AuthRes>builder()
                    .code("00")
                    .message("정상 처리")
                    .data(userService.test())
                    .build()
        );
    }
}
