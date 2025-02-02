package com.gnimtier.api.client.riot;

import com.gnimtier.api.data.dto.riot.client.request.PuuidRequestDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerLeaderboardResponseDto;
import com.gnimtier.api.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class RiotApiClient {
    @Value("${gnt.api.url}")
    private String GNT_API_URL;
    @Value("${gnt.riot.tft.summoner.ranking.url}")
    private String TFT_SUMMONER_RANKING_URL;
    private final Logger LOGGER = LoggerFactory.getLogger(RiotApiClient.class);

    public SummonerLeaderboardResponseDto getSummonerLeaderboard(PuuidRequestDto puuidRequestDto) {
        try {
            LOGGER.info("[getSummonerLeaderboard] - Getting Summoner Leaderboard");
            SummonerLeaderboardResponseDto response = WebClient
                    .create(GNT_API_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .path(TFT_SUMMONER_RANKING_URL)
                            .build(true)
                    )
                    .bodyValue(puuidRequestDto)
                    .retrieve()
                    .bodyToMono(SummonerLeaderboardResponseDto.class)
                    .block();
            LOGGER.info("[getSummonerLeaderboard] - Got Summoner Leaderboard");
            return response;
        } catch (WebClientResponseException e) {
            LOGGER.error("[getSummonerLeaderboard] : {} : {}", e.getStatusCode(), e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
