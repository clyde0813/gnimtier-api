package com.gnimtier.api.repository;


import com.gnimtier.api.data.entity.gnt.UserComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
    Page<UserComment> findAllByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);
}
