package com.msa4hipgram.domain.auth.responses;

import com.msa4hipgram.domain.user.entities.User;
import lombok.Builder;

@Builder
public record AuthRes(
        User user
        , String accessToken
) {

}
