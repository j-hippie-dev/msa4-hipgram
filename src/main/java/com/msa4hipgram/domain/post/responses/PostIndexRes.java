package com.msa4hipgram.domain.post.responses;

import com.msa4hipgram.domain.post.entities.PostMybatis;
import lombok.Builder;

import java.util.List;

@Builder
public record PostIndexRes(
        Long total
        , boolean lastPage
        , List<PostMybatis> posts
) {
}
