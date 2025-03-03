package com.gnimtier.api.data.dto.riot.internal.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserGroupSearchRequestDto {
    private String keyword;
    private boolean isOfficial;
    private String sortBy;
    private int page;
}
