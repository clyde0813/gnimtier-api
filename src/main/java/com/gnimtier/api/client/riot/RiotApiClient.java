package com.gnimtier.api.client.riot;

import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
import com.gnimtier.api.data.dto.riot.client.request.PageableRequestDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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

    public PageableResponseDto<SummonerResponseDto> getSummonerLeaderboard(PageableRequestDto<String> puuidRequestDto) {
        try {
            LOGGER.info("[getSummonerLeaderboard] - Getting Summoner Leaderboard");
            PageableResponseDto<SummonerResponseDto> response = WebClient
                    .create(GNT_API_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                            .path(TFT_SUMMONER_RANKING_URL)
                            .build(true)
                    )
                    .bodyValue(puuidRequestDto)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<PageableResponseDto<SummonerResponseDto>>() {})
                    .block();
            LOGGER.info("[getSummonerLeaderboard] - Got Summoner Leaderboard : {}", response);
            return response;
        } catch (WebClientResponseException e) {
            LOGGER.error("[getSummonerLeaderboard] : {} : {}", e.getStatusCode(), e.getMessage());
            throw new CustomException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
