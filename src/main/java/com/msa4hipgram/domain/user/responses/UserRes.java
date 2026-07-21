package com.msa4hipgram.domain.user.responses;

import com.msa4hipgram.domain.user.entities.User;
import com.msa4hipgram.global.security.constant.RolePolicy;
import java.time.LocalDateTime;

public record UserRes(
    long id
    , String email
    , String nick
    , RolePolicy role
    , String profile
    , LocalDateTime createdAt
) {
    public static UserRes from(User user) {
        return new UserRes(
            user.getId()
            , user.getEmail()
            , user.getNick()
            , user.getRole()
            , user.getProfile()
            , user.getCreatedAt()
        );
    }
}
