package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.riot.tft.client.Response.TftLeaderboardResponseDto;
import com.gnimtier.api.service.riot.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tft/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;


    @GetMapping("/by-group/{groupId}")
    public TftLeaderboardResponseDto getLeaderboard(
            @PathVariable("groupId") String groupId,
            @RequestParam(value = "sortBy", defaultValue = "tier") String sortBy,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        TftLeaderboardResponseDto tftLeaderboardResponseDto;
        switch (sortBy) {
            case "tier":
                tftLeaderboardResponseDto = leaderboardService.getTierLeaderboardByGroupId(groupId, page);
            default:
                tftLeaderboardResponseDto = leaderboardService.getTierLeaderboardByGroupId(groupId, page);
        }
        return tftLeaderboardResponseDto;
    }
}
