package com.gnimtier.api.client.riot;

import com.gnimtier.api.constant.riot.GntRiotApiConstants;
import com.gnimtier.api.constant.riot.TftApiPathConstants;
import com.gnimtier.api.data.dto.riot.tft.internal.request.PuuidRequestDto;
import com.gnimtier.api.data.dto.riot.tft.internal.response.SummonerLeaderboardResponseDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class TftApiClient {
    private final Logger logger = LoggerFactory.getLogger(TftApiClient.class);


    private void logError(String where, HttpStatusCode statusCode, String message) {
        logger.error("{}: {}: {}", where, statusCode, message);
    }

    private void logInfo(String where, String message) {
        logger.info("{}: {}", where, message);
    }

    public SummonerLeaderboardResponseDto getSummonerLeaderboard(PuuidRequestDto puuidRequestDto) {
        try {
            SummonerLeaderboardResponseDto response = WebClient
                    .create(GntRiotApiConstants.BASE_URL)
                    .post()
                    .uri(uriBuilder -> uriBuilder
                                    .scheme("https")
                                    .path(TftApiPathConstants.TFT_SUMMONER_RANKING)
                                    .build(true)
                    )
                    .bodyValue(puuidRequestDto)
                    .retrieve()
                    .bodyToMono(SummonerLeaderboardResponseDto.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            logError("TftApiClient - getSummonerLeaderboard", e.getStatusCode(), e.getMessage());
            throw e;
        }
    }
}
