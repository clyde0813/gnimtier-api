package com.gnimtier.api.data.dto.gnt;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class UserGroupDto {
    private String id;
    private String name;
    private String description;
    private String parentId;
    private Boolean isRoot;
}
