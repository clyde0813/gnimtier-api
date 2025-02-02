package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.riot.internal.LeaderboardParamDto;
import com.gnimtier.api.data.dto.riot.client.Response.RiotLeaderboardResponseDto;
import com.gnimtier.api.service.riot.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;


    @GetMapping("/by-group")
    public RiotLeaderboardResponseDto getLeaderboard(
           LeaderboardParamDto leaderboardParamDto
    ) {
        return leaderboardService.getLeaderboard(leaderboardParamDto);
    }
}
