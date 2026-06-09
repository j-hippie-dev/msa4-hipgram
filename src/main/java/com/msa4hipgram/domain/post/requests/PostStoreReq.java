package com.msa4hipgram.domain.post.requests;


import jakarta.validation.constraints.NotBlank;

public record PostStoreReq(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
        , @NotBlank(message = "업로드할 사진을 선택해주세요.")
        String image
) {
}
