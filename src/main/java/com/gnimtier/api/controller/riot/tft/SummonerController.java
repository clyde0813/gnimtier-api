package com.gnimtier.api.controller.riot.tft;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.service.riot.tft.SummonerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riot/tft")
public class SummonerController {
    private final SummonerService summonerService;

    @Tag(name = "(TFT) 소환사 조회")
    @GetMapping("/summoners")
    public DataDto<Map<String, SummonerResponseDto>> getRiotSummoner(
            @RequestParam(value = "gameName", required = true) String gameName,
            @RequestParam(value = "tagLine", required = true) String tagLine,
            @RequestParam(value = "refresh", required = false, defaultValue = "false") boolean refresh
    ) {
        return new DataDto<>(Map.of("summoner", summonerService.getSummoner(gameName, tagLine, refresh)));
    }
}
