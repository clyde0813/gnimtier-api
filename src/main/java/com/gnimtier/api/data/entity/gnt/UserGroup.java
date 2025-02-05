package com.gnimtier.api.data.entity.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
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

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "description")
    private String description;

    @Column(name = "is_official")
    private boolean isOfficial;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "is_root")
    private boolean isRoot;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserGroupDto toDto() {
        UserGroupDto dto = new UserGroupDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setParentId(parentId);
        dto.setIsRoot(isRoot);
        return dto;
    }
}
