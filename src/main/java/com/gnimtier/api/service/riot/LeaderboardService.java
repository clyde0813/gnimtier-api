package com.gnimtier.api.service.riot;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.riot.tft.client.Response.TftLeaderboardResponseDto;
import com.gnimtier.api.data.dto.riot.tft.client.Response.TftUserResponseDto;
import com.gnimtier.api.data.dto.riot.tft.internal.request.PuuidRequestDto;
import com.gnimtier.api.data.dto.riot.tft.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import com.gnimtier.api.repository.UserPuuidRepository;
import com.gnimtier.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(LeaderboardService.class);

    public TftLeaderboardResponseDto getTierLeaderboardByGroupId(String groupId, int page) {
        PuuidRequestDto puuidRequestDto = new PuuidRequestDto();

        List<String> puuidList = new ArrayList<>();
        List<UserGroupAssociation> userGroupAssociationList = userGroupAssociationRepository.findAllByGroupId(groupId);

        List<TftUserResponseDto> tftUserResponseDtoList = new ArrayList<>();
        TftLeaderboardResponseDto tftLeaderboardResponseDto = new TftLeaderboardResponseDto();

        userGroupAssociationList.forEach(userGroupAssociation -> {
            String userId = userGroupAssociation.getUserId();
            puuidList.add(userPuuidRepository
                    .findByUserId(userId)
                    .getPuuid());
        });
        puuidRequestDto.setPuuid(puuidList);
        puuidRequestDto.setSortBy("tier");
        puuidRequestDto.setPage(page);
        puuidRequestDto.setPageSize(5);
//        logger.info("puuidRequestDto: {}", puuidRequestDto);
        List<SummonerResponseDto> summonerResponseDtoList = tftApiClient
                .getSummonerLeaderboard(puuidRequestDto)
                .getLeaderboard();
        summonerResponseDtoList.forEach(responseDto -> {
            String userId = userPuuidRepository
                    .findByPuuid(responseDto.getPuuid())
                    .getUserId();
            User user = userRepository
                    .findById(userId)
                    .get();
            TftUserResponseDto tftUserResponseDto = new TftUserResponseDto(user.getUserDto(), responseDto);
            tftUserResponseDtoList.add(tftUserResponseDto);
        });
        tftLeaderboardResponseDto.setData(tftUserResponseDtoList);
        tftLeaderboardResponseDto.setPage(page);
        tftLeaderboardResponseDto.setPageSize(5);
        return tftLeaderboardResponseDto;
    }
}
