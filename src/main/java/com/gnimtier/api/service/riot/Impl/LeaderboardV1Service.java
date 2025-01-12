package com.gnimtier.api.service.riot.Impl;

import com.gnimtier.api.client.riot.TftApiClient;
import com.gnimtier.api.data.dto.riot.tft.client.Response.TftLeaderboardResponseDto;
import com.gnimtier.api.data.dto.riot.tft.client.Response.TftUserResponseDto;
import com.gnimtier.api.data.dto.riot.tft.internal.request.PuuidRequestDto;
import com.gnimtier.api.data.dto.riot.tft.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.data.entity.riot.UserPuuid;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import com.gnimtier.api.repository.UserPuuidRepository;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.riot.LeaderboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardV1Service implements LeaderboardService {
    private final UserRepository userRepository;
    private final UserPuuidRepository userPuuidRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupAssociationRepository userGroupAssociationRepository;

    private final TftApiClient tftApiClient;

    private final Logger logger = LoggerFactory.getLogger(LeaderboardV1Service.class);

    @Autowired
    public LeaderboardV1Service(
            UserRepository userRepository,
            UserPuuidRepository userPuuidRepository,
            UserGroupRepository userGroupRepository,
            UserGroupAssociationRepository userGroupAssociationRepository,
            TftApiClient tftApiClient
    ) {
        this.userRepository = userRepository;
        this.userPuuidRepository = userPuuidRepository;
        this.userGroupRepository = userGroupRepository;
        this.userGroupAssociationRepository = userGroupAssociationRepository;
        this.tftApiClient = tftApiClient;
    }

    @Override
    public TftLeaderboardResponseDto getTierLeaderboardByGroupId(String groupId) {
        PuuidRequestDto puuidRequestDto = new PuuidRequestDto();

        List<String> puuidList = new ArrayList<>();
        List<UserGroupAssociation> userGroupAssociationList = userGroupAssociationRepository.findAllByUserGroupId(groupId);

        List<TftUserResponseDto> tftUserResponseDtoList = new ArrayList<>();
        TftLeaderboardResponseDto tftLeaderboardResponseDto = new TftLeaderboardResponseDto();

        userGroupAssociationList.forEach(userGroupAssociation -> puuidList.add(userPuuidRepository.findByUserId(userGroupAssociation.getUser().getId()).getPuuid()));
        puuidRequestDto.setPuuid(puuidList);
        puuidRequestDto.setSortBy("tier");
        puuidRequestDto.setPage(0);
        puuidRequestDto.setPageSize(20);
//        logger.info("puuidRequestDto: {}", puuidRequestDto);
        List<SummonerResponseDto> summonerResponseDtoList = tftApiClient.getSummonerLeaderboard(puuidRequestDto).getLeaderboard();
        summonerResponseDtoList.forEach(responseDto -> {
            User user = userPuuidRepository.findByPuuid(responseDto.getPuuid()).getUser();
            TftUserResponseDto tftUserResponseDto = new TftUserResponseDto(user.toUserDto(), responseDto);
            tftUserResponseDtoList.add(tftUserResponseDto);
        });
        tftLeaderboardResponseDto.setData(tftUserResponseDtoList);
        tftLeaderboardResponseDto.setPage(0);
        tftLeaderboardResponseDto.setPageSize(20);
        return tftLeaderboardResponseDto;
    }
}
