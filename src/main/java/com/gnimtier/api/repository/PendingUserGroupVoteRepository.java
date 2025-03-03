package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.gnt.PendingUserGroupVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PendingUserGroupVoteRepository extends JpaRepository<PendingUserGroupVote, Long> {
    Boolean existsByUserIdAndPendingUserGroupId(String userId, String pendingUserGroupId);

    Optional<PendingUserGroupVote> findFirstByUserIdOrderByCreatedAtDesc(String userId);
}
