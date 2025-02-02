package com.gnimtier.api.data.dto.riot.client.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiotLeaderboardResponseDto {
    private List<RiotUserResponseDto> data;
    private int page;
    private int pageSize;
}