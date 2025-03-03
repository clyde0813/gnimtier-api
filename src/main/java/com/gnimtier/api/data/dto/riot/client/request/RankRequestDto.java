package com.gnimtier.api.data.dto.riot.client.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankRequestDto {
    private List<String> puuidList;
    private String puuid;
}
