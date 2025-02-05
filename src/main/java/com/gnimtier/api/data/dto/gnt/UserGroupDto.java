package com.gnimtier.api.data.dto.gnt;

import lombok.Data;

import java.util.Optional;

@Data
public class UserGroupDto {
    private String id;
    private String name;
    private String description;
    private String parentId;
    private Boolean isRoot;
}
