package com.gnimtier.api.service.riot;

import com.gnimtier.api.data.dto.riot.tft.client.Response.TftLeaderboardResponseDto;

public interface LeaderboardService {
    TftLeaderboardResponseDto getTierLeaderboardByGroupId(String groupId);
}
