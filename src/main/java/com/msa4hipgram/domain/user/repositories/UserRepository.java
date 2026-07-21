package com.msa4hipgram.domain.user.repositories;

import com.msa4hipgram.domain.user.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
