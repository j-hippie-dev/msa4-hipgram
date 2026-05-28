package com.msa4hipgram.domain.auth.responses;

import com.msa4hipgram.domain.user.responses.UserRes;
import lombok.Builder;

@Builder
public record AuthRes(
        UserRes user
        , String accessToken
) {

}
