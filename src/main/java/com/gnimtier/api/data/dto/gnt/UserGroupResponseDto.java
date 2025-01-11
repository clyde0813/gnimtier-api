package com.gnimtier.api.data.dto.gnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGroupResponseDto {
    private String id;
    private String name;
    private String description;
    private String category;
}
