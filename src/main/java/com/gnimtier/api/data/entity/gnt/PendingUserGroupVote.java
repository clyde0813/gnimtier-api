package com.gnimtier.api.data.entity.gnt;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pending_user_group_vote")
public class PendingUserGroupVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "pending_user_group_id")
    private String pendingUserGroupId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
