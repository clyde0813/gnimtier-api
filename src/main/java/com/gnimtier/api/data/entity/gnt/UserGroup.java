package com.gnimtier.api.data.entity.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupRankDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "is_official")
    private boolean isOfficial;

    @Column(name = "is_joinable")
    private boolean isJoinable;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserGroupDto toDto() {
        return UserGroupDto
                .builder()
                .id(id)
                .name(name)
                .description(description)
                .parentId(parentId)
                .isOfficial(isOfficial)
                .isJoinable(isJoinable)
                .build();
    }

    public UserGroupRankDto toDto(int rank, int userCount) {
        return UserGroupRankDto
                .builder()
                .rank(rank)
                .userCount(userCount)
                .id(id)
                .name(name)
                .description(description)
                .parentId(parentId)
                .isOfficial(isOfficial)
                .isJoinable(isJoinable)
                .build();
    }
}
