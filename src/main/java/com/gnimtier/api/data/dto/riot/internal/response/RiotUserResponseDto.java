package com.gnimtier.api.data.dto.riot.internal.response;

import com.gnimtier.api.data.dto.gnt.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RiotUserResponseDto {
    private UserDto user;
    private SummonerResponseDto summoner;
}
