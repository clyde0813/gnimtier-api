package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.riot.client.Response.PageableDto;
import com.gnimtier.api.data.dto.riot.client.Response.RiotUserResponseDto;
import com.gnimtier.api.data.dto.riot.internal.LeaderboardParamDto;
import com.gnimtier.api.service.riot.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;


    @GetMapping("/by-group")
    public PageableDto<RiotUserResponseDto> getLeaderboard(
           LeaderboardParamDto leaderboardParamDto
    ) {
        return leaderboardService.getLeaderboard(leaderboardParamDto);
    }
}
