package com.gnimtier.api.controller.riot.tft;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
import com.gnimtier.api.data.dto.riot.internal.response.RiotUserResponseDto;
import com.gnimtier.api.data.dto.riot.internal.request.LeaderboardRequestDto;
import com.gnimtier.api.service.riot.tft.LeaderboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riot/tft/leaderboard")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @Tag(name = "(TFT) 리더보드 조회")
    @GetMapping("/by-group")
    public DataDto<PageableResponseDto<RiotUserResponseDto>> getLeaderboard(
            LeaderboardRequestDto leaderboardRequestDto
    ) {
        return new DataDto<>(leaderboardService.getLeaderboard(leaderboardRequestDto));
    }
}
