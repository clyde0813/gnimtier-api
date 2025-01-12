package com.gnimtier.api.data.dto.riot.tft.client.Response;

import com.gnimtier.api.data.dto.gnt.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TftLeaderboardResponseDto {
    private List<TftUserResponseDto> data;
    private int page;
    private int pageSize;
}