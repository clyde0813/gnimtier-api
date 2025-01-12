package com.gnimtier.api.controller.tft;

import com.gnimtier.api.data.dto.riot.tft.client.Response.TftLeaderboardResponseDto;
import com.gnimtier.api.service.riot.Impl.LeaderboardV1Service;
import com.gnimtier.api.service.riot.LeaderboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tft/leaderboard")
public class LeaderboardV1Controller {
    private final LeaderboardService leaderboardService;

    @Autowired
    public LeaderboardV1Controller(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }


    @GetMapping("/by-group/{groupId}")
    public TftLeaderboardResponseDto getLeaderboard(
            @PathVariable("groupId") String groupId,
            @RequestParam("sortBy") String sortBy
    ) {
        TftLeaderboardResponseDto tftLeaderboardResponseDto;
        switch (sortBy) {
            case "tier":
                tftLeaderboardResponseDto = leaderboardService.getTierLeaderboardByGroupId(groupId);
            default:
                tftLeaderboardResponseDto = leaderboardService.getTierLeaderboardByGroupId(groupId);
        }
        return tftLeaderboardResponseDto;
    }
}
