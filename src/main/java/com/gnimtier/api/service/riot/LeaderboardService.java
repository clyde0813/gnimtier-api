package com.gnimtier.api.service.riot;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.riot.client.Response.PageableDto;
import com.gnimtier.api.data.dto.riot.client.Response.RiotUserResponseDto;
import com.gnimtier.api.data.dto.riot.internal.LeaderboardParamDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import com.gnimtier.api.repository.UserPuuidRepository;
import com.gnimtier.api.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserPuuidRepository userPuuidRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupAssociationRepository userGroupAssociationRepository;

    private final RiotApiClient tftApiClient;

    private final Logger LOGGER = LoggerFactory.getLogger(LeaderboardService.class);
    private final UserService userService;

    public PageableDto<RiotUserResponseDto> getLeaderboard(LeaderboardParamDto leaderboardParamDto) {
        PageableDto<String> puuidRequestDto = new PageableDto<>();

        // gnt riot api에 보낼 puuid list
        List<String> puuidList = new ArrayList<>();
        // 그룹 - 유저 간 연결 조회
        List<UserGroupAssociation> userGroupAssociationList = userGroupAssociationRepository.findAllByGroupId(leaderboardParamDto.getGroupId());

        List<RiotUserResponseDto> riotUserResponseDtoList = new ArrayList<>();
        PageableDto<RiotUserResponseDto> riotLeaderboardResponseDto = new PageableDto<>();
        userGroupAssociationList.forEach(association -> puuidList.add(association.getPuuid()));


        puuidRequestDto.setData(puuidList);
        puuidRequestDto.setSortBy(leaderboardParamDto.getSortBy());
        puuidRequestDto.setPage(leaderboardParamDto.getPage());
        puuidRequestDto.setPageSize(5);
        PageableDto<SummonerResponseDto> summonerResponseDtoList = tftApiClient
                .getSummonerLeaderboard(puuidRequestDto);
        summonerResponseDtoList.getData().forEach(responseDto -> {
            String userId = userPuuidRepository
                    .findByPuuid(responseDto.getPuuid())
                    .getUserId();
            User user;
            try {
                user = userService.getUserByUserId(userId);
            } catch (Exception e) {
                throw new CustomException("User Not Found", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            RiotUserResponseDto tftUserResponseDto = new RiotUserResponseDto(user.getUserDto(), responseDto);
            riotUserResponseDtoList.add(tftUserResponseDto);
        });
        riotLeaderboardResponseDto.setData(riotUserResponseDtoList);
        riotLeaderboardResponseDto.setPage(leaderboardParamDto.getPage());
        riotLeaderboardResponseDto.setPageSize(5);
        return riotLeaderboardResponseDto;
    }
}
