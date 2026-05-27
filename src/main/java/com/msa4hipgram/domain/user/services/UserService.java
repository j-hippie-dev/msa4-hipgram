package com.msa4hipgram.domain.user.services;

import com.msa4hipgram.domain.auth.responses.AuthRes;
import com.msa4hipgram.domain.user.entities.User;
import com.msa4hipgram.domain.user.mapper.UserMapper;
import com.msa4hipgram.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final JwtProvider jwtProvider;

    public AuthRes test() {
        // 유저 획득
        User user = userMapper.findByPk(17);

        String newAccessToken = jwtProvider.generateAccessToken(user);
        String newRefreshToken = jwtProvider.generateRefreshToken(user);

        System.out.println(newRefreshToken);

        return AuthRes.builder()
                .user(user)
                .accessToken(newAccessToken)
                .build();
    }
}
