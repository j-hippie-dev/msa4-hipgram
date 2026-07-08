package com.msa4hipgram.domain.post.repositories;

import com.msa4hipgram.domain.post.entities.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.msa4hipgram.domain.post.entities.QPost.post;
import static com.msa4hipgram.domain.user.entities.QUser.user;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    //SELECT *
    //FROM posts
    //  JOIN users
    //      ON posts.user_id = users.id
    //WHERE deleted_at IS NULL <- JAP에서 이미 설정. 안 써줘도 됨.
    //ORDER BY created_at desc, id desc
    //LIMIT ? OFFSET ?
    public List<Post> pagination(int offset, int limit) {
        return jpaQueryFactory
            .selectFrom(post)
            .join(post.user, user).fetchJoin()
            .orderBy(post.createdAt.desc(), post.id.desc())
            .limit(limit)
            .offset(offset)
            .fetch();
    }
}
