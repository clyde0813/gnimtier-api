package com.gnimtier.api.service.riot.tft;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.service.gnt.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummonerService {
    private final RiotApiClient riotApiClient;

    private final Logger LOGGER = LoggerFactory.getLogger(SummonerService.class);

    public SummonerResponseDto getSummoner(String puuid, boolean refresh) {
        LOGGER.info("[getSummoner] - Getting Summoner");
        SummonerResponseDto summonerResponseDto = riotApiClient.getSummoner(puuid, refresh);
        LOGGER.info("[getSummoner] - Got Summoner : {}", summonerResponseDto.getPuuid());
        return summonerResponseDto;
    }

    public SummonerResponseDto getSummoner(String gameName, String tagLine, boolean refresh) {
        LOGGER.info("[getSummoner] - Getting Summoner");
        SummonerResponseDto summonerResponseDto = riotApiClient.getSummoner(gameName, tagLine, refresh);
        LOGGER.info("[getSummoner] - Got Summoner : {}", summonerResponseDto.getPuuid());
        return summonerResponseDto;
    }
}
