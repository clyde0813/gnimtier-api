package com.gnimtier.api.controller.riot.tft;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
import com.gnimtier.api.data.dto.riot.client.Response.RiotUserResponseDto;
import com.gnimtier.api.data.dto.riot.internal.LeaderboardParamDto;
import com.gnimtier.api.service.riot.tft.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riot/tft/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;


    @GetMapping("/by-group")
    public DataDto<PageableResponseDto<RiotUserResponseDto>> getLeaderboard(
           LeaderboardParamDto leaderboardParamDto
    ) {
        return new DataDto<>(leaderboardService.getLeaderboard(leaderboardParamDto));
    }
}
