package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.gnt.PendingUserGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingUserGroupRepository extends JpaRepository<PendingUserGroup, String> {
    Page<PendingUserGroup> findAllByOrderByVoteCountDesc(Pageable pageable);
    Page<PendingUserGroup> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<PendingUserGroup> findAllByOrderByNameAsc(Pageable pageable);

}
