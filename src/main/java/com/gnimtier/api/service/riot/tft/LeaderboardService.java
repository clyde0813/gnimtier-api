package com.gnimtier.api.service.riot.tft;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.riot.client.PageableDto;
import com.gnimtier.api.data.dto.riot.internal.response.RiotUserResponseDto;
import com.gnimtier.api.data.dto.riot.internal.request.LeaderboardRequestDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserPuuidRepository;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final UserPuuidRepository userPuuidRepository;
    private final UserGroupAssociationRepository userGroupAssociationRepository;

    private final RiotApiClient tftApiClient;

    private final Logger LOGGER = LoggerFactory.getLogger(LeaderboardService.class);

    public PageableDto.PageableResponseDto<RiotUserResponseDto> getLeaderboard(LeaderboardRequestDto leaderboardRequestDto) {
        LOGGER.info("[getLeaderboard] - Getting Leaderboard");
        // gnt riot 에 보낼 요청 body
        PageableDto.PageableRequestDto<String> puuidRequestDto = new PageableDto.PageableRequestDto<>();

        // gnt riot api에 보낼 puuid list
        List<String> puuidList = userGroupService.getPuuidList(leaderboardRequestDto.getGroupId());

        List<RiotUserResponseDto> riotUserResponseDtoList = new ArrayList<>();
        PageableDto.PageableResponseDto<RiotUserResponseDto> riotLeaderboardResponseDto = new PageableDto.PageableResponseDto<>();


        puuidRequestDto.setData(puuidList);
        puuidRequestDto.setSortBy(leaderboardRequestDto.getSortBy());
        puuidRequestDto.setPage(leaderboardRequestDto.getPage());
        puuidRequestDto.setPageSize(5);

        PageableDto.PageableResponseDto<SummonerResponseDto> summonerResponseDtoList = tftApiClient.getSummonerLeaderboard(puuidRequestDto);

        summonerResponseDtoList
                .getData()
                .forEach(responseDto -> {
                    String userId = userPuuidRepository
                            .findByPuuid(responseDto.getPuuid())
                            .get()
                            .getUserId();
                    User user;
                    try {
                        user = userService.getUserByUserId(userId);
                    } catch (Exception e) {
                        LOGGER.error("[getLeaderboard] - User Not Found");
                        throw new CustomException("User Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    RiotUserResponseDto tftUserResponseDto = new RiotUserResponseDto(user.getUserDto(), responseDto);
                    riotUserResponseDtoList.add(tftUserResponseDto);
                });
        riotLeaderboardResponseDto.setData(riotUserResponseDtoList);
        riotLeaderboardResponseDto.setPage(summonerResponseDtoList.getPage());
        riotLeaderboardResponseDto.setPageSize(summonerResponseDtoList.getPageSize());
        riotLeaderboardResponseDto.setHasNext(summonerResponseDtoList.getHasNext());
        riotLeaderboardResponseDto.setHasPrevious(summonerResponseDtoList.getHasPrevious());
        LOGGER.info("[getLeaderboard] - Got Leaderboard");
        return riotLeaderboardResponseDto;
    }
}
