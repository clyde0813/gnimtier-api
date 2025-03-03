package com.gnimtier.api.data.dto.gnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingUserGroupRequestDto {
    private String name;
    private String description;
}
