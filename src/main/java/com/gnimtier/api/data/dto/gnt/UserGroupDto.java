package com.gnimtier.api.data.dto.gnt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserGroupDto {
    private String id;
    private String name;
    private String description;
    private String parentId;
    private Boolean isOfficial;
    private Boolean isJoinable;
}

