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
@Table(name = "user_group")
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "group_category_id")
    private UserGroupCategory groupCategory;

    @Column(name = "description")
    private String description;

    @Column(name = "is_official")
    private boolean isOfficial;

    @ManyToOne
    private UserGroup parent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
