package com.msa4hipgram.domain.post.responses;

import com.msa4hipgram.domain.post.entities.Post;

import java.util.List;

public record PostIndexRes(
        Long total
        , boolean lastPage
        , List<PostWithUserRes> posts
) {
    public static PostIndexRes from(long total, boolean lastPage, List<Post> posts) {
        // 내가 전달한 값들로부터. 그래서 from
        return new PostIndexRes(
            total
            , lastPage
            , posts.stream().map(PostWithUserRes::from).toList()
        );
    }
}
