package com.msa4hipgram.domain.auth.services;

import com.msa4hipgram.domain.auth.requests.LoginReq;
import com.msa4hipgram.domain.user.entities.User;
import com.msa4hipgram.domain.user.mapper.UserMapper;
import com.msa4hipgram.global.errors.custom.NotRegisteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserMapper userMapper;

    public void login(LoginReq loginReq) {
        // 유저 정보 획득
        User user = userMapper.findByEmail(loginReq.email());

        // 유저 가입 여부 확인
        if(user == null) {
            throw new NotRegisteredException("아이디와 비밀번호를 확인해주세요.");
        }

        // 비밀번호 체크

        // 토큰 생성

        // 리프레쉬 토큰 DB에 저장 // 우리는 DB에 저장할거임.

        // 리프레쉬 토큰 Cookie에 저장

        // 리턴
    }
}
