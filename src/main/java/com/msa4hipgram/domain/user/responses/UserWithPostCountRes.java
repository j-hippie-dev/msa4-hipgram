package com.msa4hipgram.domain.user.responses;

import com.msa4hipgram.domain.user.entities.User;

public record UserWithPostCountRes(
    UserRes user
    , long countPosts
) {
    public static UserWithPostCountRes from(User user, long countPost) {
        return new UserWithPostCountRes(
            UserRes.from(user)
            , countPost
        );
    }
}
