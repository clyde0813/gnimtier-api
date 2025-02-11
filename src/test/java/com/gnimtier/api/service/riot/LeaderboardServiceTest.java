//package com.gnimtier.api.service.riot;
//
//import com.gnimtier.api.client.riot.RiotApiClient;
//import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
//import com.gnimtier.api.data.dto.riot.client.Response.RiotUserResponseDto;
//import com.gnimtier.api.data.dto.riot.client.request.PageableRequestDto;
//import com.gnimtier.api.data.dto.riot.internal.LeaderboardParamDto;
//import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
//import com.gnimtier.api.data.entity.auth.User;
//import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
//import com.gnimtier.api.exception.CustomException;
//import com.gnimtier.api.repository.UserGroupAssociationRepository;
//import com.gnimtier.api.repository.UserPuuidRepository;
//import com.gnimtier.api.service.gnt.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("리더보드 서비스 단위 테스트")
//class LeaderboardServiceTest {
//
//    @Mock
//    private UserGroupAssociationRepository userGroupAssociationRepository;
//
//    @Mock
//    private UserPuuidRepository userPuuidRepository;
//
//    @Mock
//    private RiotApiClient tftApiClient;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private LeaderboardService leaderboardService;
//
//
//    @Test
//    void getLeaderboardReturnsCorrectResponse() {
//        LeaderboardParamDto leaderboardParamDto = new LeaderboardParamDto();
//        leaderboardParamDto.setGroupId("1");
//        leaderboardParamDto.setSortBy("tier");
//        leaderboardParamDto.setPage(0);
//
//        UserGroupAssociation association = new UserGroupAssociation();
//        association.setPuuid("puuid1");
//
//        when(userGroupAssociationRepository.findAllByGroupId("123")).thenReturn(List.of(association));
//
//        SummonerResponseDto summonerResponseDto = new SummonerResponseDto();
//        summonerResponseDto.setPuuid("puuid1");
//
//        PageableResponseDto<SummonerResponseDto> summonerResponse = new PageableResponseDto<>();
//        summonerResponse.setData(List.of(summonerResponseDto));
//        summonerResponse.setTotalPages(1);
//        summonerResponse.setTotalElements(1);
//        summonerResponse.setHasNext(false);
//        summonerResponse.setHasPrevious(false);
//
//        when(tftApiClient.getSummonerLeaderboard(any(PageableRequestDto.class))).thenReturn(summonerResponse);
//
//        User user = new User();
//        user.setUserId("userId1");
//
//        when(userPuuidRepository.findByPuuid("puuid1")).thenReturn(user);
//        when(userService.getUserByUserId("userId1")).thenReturn(user);
//
//        PageableResponseDto<RiotUserResponseDto> result = leaderboardService.getLeaderboard(leaderboardParamDto);
//
//        assertNotNull(result);
//        assertEquals(1, result
//                .getData()
//                .size());
//        assertEquals(1, result.getTotalPages());
//        assertEquals(1, result.getTotalElements());
//        assertFalse(result.getHasNext());
//        assertFalse(result.getHasPrevious());
//    }
//}