package com.gnimtier.api.data.dto.riot.internal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SummonerResponseDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private String id;
    private String accountId;
    private int profileIconId;
    private Long revisionDate;
    private Long summonerLevel;
    //    private LocalDateTime updatedAt;
    //    private LocalDateTime renewedAt;
    private Map<String, LeagueEntryResponseDto> entry;
}
