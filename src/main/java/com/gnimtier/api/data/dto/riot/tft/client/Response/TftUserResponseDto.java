package com.gnimtier.api.data.dto.riot.tft.client.Response;

import com.gnimtier.api.data.dto.gnt.UserDto;
import com.gnimtier.api.data.dto.riot.tft.internal.response.SummonerResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TftUserResponseDto {
    private UserDto user;
    private SummonerResponseDto summoner;
}
