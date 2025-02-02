package com.gnimtier.api.data.dto.riot.internal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeagueEntryResponseDto {
    private int tier;
    private int rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private Boolean veteran;
    private Boolean inactive;
    private Boolean freshBlood;
    private Boolean hotStreak;
    private String leagueId;
    private String queueType;
}