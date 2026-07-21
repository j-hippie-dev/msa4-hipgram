package com.msa4hipgram.domain.post.services;

import com.msa4hipgram.domain.post.entities.Post;
import com.msa4hipgram.domain.post.repositories.PostQueryRepository;
import com.msa4hipgram.domain.post.repositories.PostRepository;
import com.msa4hipgram.domain.post.requests.PostIndexReq;
import com.msa4hipgram.domain.post.responses.PostIndexRes;
import com.msa4hipgram.domain.post.responses.PostWithUserRes;
import com.msa4hipgram.global.errors.custom.DeletedRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;

    public PostIndexRes index(PostIndexReq postIndexReq) {
        int offset = (postIndexReq.page() - 1) * postIndexReq.limit();

        // 특정 페이지 게시글 조회
        List<Post> result = postQueryRepository.pagination(offset, postIndexReq.limit());

        // 토탈 획득
        long total = postRepository.count();
        boolean lastPage = offset + postIndexReq.limit() >= total;

        // 컨트롤러 전달
        return PostIndexRes.from(total, lastPage, result);
    }

    public PostWithUserRes show(long id) {
        Post result = postRepository.findById(id)
            .orElseThrow(() -> new DeletedRecordException("이미 삭제된 게시글입니다."));

        return PostWithUserRes.from(result);
    }
}
