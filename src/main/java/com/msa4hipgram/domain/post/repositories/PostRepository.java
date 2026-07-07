package com.msa4hipgram.domain.post.repositories;

import com.msa4hipgram.domain.post.entities.Post;
import com.msa4hipgram.domain.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    long countByUser(User user);
}
