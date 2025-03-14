package com.gnimtier.api.data.entity.gnt;

import com.gnimtier.api.data.dto.gnt.PendingUserGroupDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pending_user_group")
public class PendingUserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    // 반정규화
    @Column(name = "vote_count")
    private int voteCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public PendingUserGroupDto.PendingUserGroupResponseDto toDto() {
        return PendingUserGroupDto.PendingUserGroupResponseDto
                .builder()
                .id(id)
                .name(name)
                .description(description)
                .voteCount(voteCount)
                .createdAt(createdAt)
                .build();
    }
}
