package com.gnimtier.api.data.dto.riot.internal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummonerLeaderboardResponseDto {
    private List<SummonerResponseDto> leaderboard;
    private int pageSize;
    private int page;
}
