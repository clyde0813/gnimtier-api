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
//그님티 riot api 서버에 leaderboard 요청할때 보내는 dto
// puuid 리스트와 Page 보내면 정렬해서 리턴옴
public class PuuidRequestDto {
    private List<String> puuid;
    private String sortBy;
    private int pageSize;
    private int page;
}
