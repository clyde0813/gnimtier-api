package com.gnimtier.api.data.dto.riot.internal.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaderboardRequestDto {
    private String groupId;
    private String sortBy;
    private int page;
}
