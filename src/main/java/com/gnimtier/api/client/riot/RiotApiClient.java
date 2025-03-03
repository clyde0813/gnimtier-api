package com.gnimtier.api.client.riot;

import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
import com.gnimtier.api.data.dto.riot.client.request.PageableRequestDto;
import com.gnimtier.api.data.dto.riot.client.request.RankRequestDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RiotApiClient {
    @Value("${gnt.api.url}")
    private String GNT_API_URL;
    @Value("${gnt.riot.tft.summoner.puuid}")
    private String TFT_SUMMONER_PUUID;
    @Value("${gnt.riot.tft.summoner.riotId}")
    private String TFT_SUMMONER_RIOT_ID;
    @Value("${gnt.riot.tft.summoner.leaderboards.puuid}")
    private String TFT_SUMMONER_RANKING_URL;
    @Value("${gnt.riot.tft.summoner.ranks.puuid}")
    private String TFT_SUMMONER_RANKS_URL;
    private final Logger LOGGER = LoggerFactory.getLogger(RiotApiClient.class);

    public SummonerResponseDto getSummoner(String puuid, boolean refresh) {
        LOGGER.info("[getSummoner] - Getting Summoner");
        SummonerResponseDto response = WebClient
                .create(GNT_API_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(TFT_SUMMONER_PUUID + "/" + puuid)
                        .queryParam("refresh", refresh)
                        .build(true))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.error(new CustomException("Not Found", HttpStatus.NOT_FOUND)))
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new CustomException("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS)))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException("Bad Request", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException("Riot API Error", HttpStatus.BAD_REQUEST)))
                .bodyToMono(SummonerResponseDto.class)
                .block();
        return response;
    }

    public SummonerResponseDto getSummoner(String gameName, String tagLine, boolean refresh) {
        LOGGER.info("[getSummoner] - Getting Summoner");
        SummonerResponseDto response = WebClient
                .create(GNT_API_URL)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(TFT_SUMMONER_RIOT_ID + "/" + gameName + "/" + tagLine)
                        .queryParam("refresh", refresh)
                        .build(true))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, clientResponse -> Mono.error(new CustomException("Not Found", HttpStatus.NOT_FOUND)))
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new CustomException("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS)))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException("Bad Request", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException("Riot API Error", HttpStatus.BAD_REQUEST)))
                .bodyToMono(SummonerResponseDto.class)
                .block();
        return response;
    }

    public PageableResponseDto<SummonerResponseDto> getSummonerLeaderboard(PageableRequestDto<String> puuidRequestDto) {
        LOGGER.info("[getSummonerLeaderboard] - Getting Summoner Leaderboard");
        PageableResponseDto<SummonerResponseDto> response = WebClient
                .create(GNT_API_URL)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(TFT_SUMMONER_RANKING_URL)
                        .build(true))
                .bodyValue(puuidRequestDto)
                .retrieve()
                .onStatus(HttpStatus.TOO_MANY_REQUESTS::equals, clientResponse -> Mono.error(new CustomException("TOO_MANY_REQUESTS", HttpStatus.TOO_MANY_REQUESTS)))
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException("Bad Request", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException("Riot API Error", HttpStatus.BAD_REQUEST)))
                .bodyToMono(new ParameterizedTypeReference<PageableResponseDto<SummonerResponseDto>>() {
                })
                .block();
        LOGGER.info("[getSummonerLeaderboard] - Got Summoner Leaderboard");
        return response;
    }

    public Integer getSummonerRank(RankRequestDto rankRequestDto){
        LOGGER.info("[getSummonerRank] - Getting Summoner Rank");
        Integer response = WebClient
                .create(GNT_API_URL)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(TFT_SUMMONER_RANKS_URL)
                        .build(true))
                .bodyValue(rankRequestDto)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new CustomException("Bad Request", HttpStatus.BAD_REQUEST)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new CustomException("Riot API Error", HttpStatus.BAD_REQUEST)))
                .bodyToMono(Integer.class)
                .block();
        LOGGER.info("[getSummonerRank] - Got Summoner Rank");
        return response;
    }
}
