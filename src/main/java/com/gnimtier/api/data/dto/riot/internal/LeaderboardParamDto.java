package com.gnimtier.api.data.dto.riot.internal;

import lombok.Data;

@Data
public class LeaderboardParamDto {
    private String groupId;
    private String sortBy;
    private int page;
}
