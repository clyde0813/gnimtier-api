package com.gnimtier.api.service.gnt;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.gnt.PendingUserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupRankDto;
import com.gnimtier.api.data.dto.riot.client.PageableDto;
import com.gnimtier.api.data.dto.riot.client.request.RankRequestDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.PendingUserGroup;
import com.gnimtier.api.data.entity.gnt.PendingUserGroupVote;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.data.entity.riot.UserPuuid;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final RiotApiClient riotApiClient;
    private final UserGroupAssociationRepository userGroupAssociationRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserPuuidRepository userPuuidRepository;

    private final PendingUserGroupRepository pendingUserGroupRepository;
    private final PendingUserGroupVoteRepository pendingUserGroupVoteRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(UserGroupService.class);
    private final UserRepository userRepository;

    // 부모 그룹 id로 하위 그룹 리스트 리턴
    public List<UserGroupDto> getUserGroupsByParentId(String parentId) {
        List<UserGroup> userGroupList = new ArrayList<>(userGroupRepository.findAllByParentId(parentId));
        LOGGER.info("UserGroupService: getUserGroupsByParentId: {}", userGroupList);
        List<UserGroupDto> userGroupDtoList = new ArrayList<>(userGroupList.size());
        userGroupList.forEach(userGroup -> userGroupDtoList.add(userGroup.toDto()));
        return userGroupDtoList;
    }


    // 사용자가 가입한 그룹 리스트 리턴
    public List<UserGroupRankDto> getUserGroups(String userId) {
        LOGGER.info("[UserGroupService] - getUserGroups : {}", userId);
        // 가입한 그룹이 없는 경우
        if (userGroupAssociationRepository
                .findAllByUserId(userId)
                .isEmpty()) {
            LOGGER.error("[UserGroupService] - getUserGroups : no group found");
            return new ArrayList<>();
        }

        // 가입한 그룹 존재
        List<UserGroupRankDto> userGroups = new ArrayList<>();
        String puuid = userPuuidRepository
                .findByUserId(userId)
                .orElseThrow(() -> new CustomException("Riot Account Not Found", HttpStatus.BAD_REQUEST))
                .getPuuid();
        userGroupAssociationRepository
                .findAllByUserId(userId)
                .forEach(userGroupAssociation -> {
                    String groupId = userGroupAssociation.getGroupId();
                    UserGroup userGroup = userGroupRepository.findById(groupId);
                    Integer rank = riotApiClient.getSummonerRank(new RankRequestDto(getPuuidList(groupId), puuid));
                    Integer userCount = getGroupUserCount(groupId);
                    userGroups.add(userGroup.toDto(rank, userCount));
                });
        LOGGER.info("[UserGroupService] - getUserGroups : {}", userGroups);
        return userGroups;
    }


    // 그룹 가입
    public void joinGroup(User user, String groupId) {
        LOGGER.info("[UserGroupService] - joinGroup : {}", user.getId());
        // 라이엇 계정 등록 확인
        if (!userPuuidRepository.existsByUserId(user.getId())) {
            LOGGER.error("[UserGroupService] - joinGroup failed : riot account not found");
            throw new CustomException("RIOT ACCOUNT NOT FOUND", HttpStatus.FORBIDDEN);
        }

        // 중복 가입 금지
        if (userGroupAssociationRepository.existsByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - joinGroup failed : already joined group");
            throw new CustomException("ALREADY JOINED GROUP.", HttpStatus.BAD_REQUEST);
        }

        // 유효하지 않은 그룹 id 가입 금지
        if (!userGroupRepository.existsUserGroupById(groupId)) {
            LOGGER.error("[UserGroupService] - joinGroup failed : invalid group");
            throw new CustomException("INVALID GROUP.", HttpStatus.BAD_REQUEST);
        }

        // isJoinable false 그룹 가입 금지
        if (userGroupRepository.existsUserGroupById(groupId) && !userGroupRepository
                .findById(groupId)
                .isJoinable()) {
            LOGGER.error("[UserGroupService] - joinGroup failed : not joinable group");
            throw new CustomException("INVALID GROUP.", HttpStatus.BAD_REQUEST);
        }

        UserGroupAssociation userGroupAssociation = new UserGroupAssociation();
        userGroupAssociation.setUserId(user.getId());
        userGroupAssociation.setGroupId(groupId);
        userGroupAssociationRepository.save(userGroupAssociation);
        LOGGER.info("[UserGroupService] - joinGroup success : {}", userGroupAssociation);
    }

    @Transactional
    public void leaveGroup(User user, String groupId) {
        LOGGER.info("[UserGroupService] - leaveGroup : {}", user.getId());
        // 가입되지 않은 그룹 탈퇴 불가
        if (!userGroupAssociationRepository.existsByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - leaveGroup failed : not joined group");
            throw new CustomException("NOT JOINED GROUP.", HttpStatus.BAD_REQUEST);
        }

        userGroupAssociationRepository.deleteByUserIdAndGroupId(user.getId(), groupId);
        LOGGER.info("[UserGroupService] - leaveGroup success : {}", user.getId());
    }


    // 투표중인 그룹 검색
    public PageableDto.PageableResponseDto<PendingUserGroupDto.PendingUserGroupResponseDto> getPendingGroups(String sortBy, int page) {
        LOGGER.info("[UserGroupService] - getPendingGroups : sortBy={}, page={}", sortBy, page);
        int PAGE_SIZE = 5;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 정렬 조건에 따라 Repository 메서드 매핑
        Map<String, Function<Pageable, Page<PendingUserGroup>>> sortMethodMap = Map.of("votes", pendingUserGroupRepository::findAllByOrderByVoteCountDesc, "createdAt", pendingUserGroupRepository::findAllByOrderByCreatedAtDesc, "name", pendingUserGroupRepository::findAllByOrderByNameAsc);

        // 존재하지 않는 sortBy일 경우 기본 정렬 적용
        Page<PendingUserGroup> pendingUserGroupsPage = sortMethodMap
                .getOrDefault(sortBy, pendingUserGroupRepository::findAllByOrderByVoteCountDesc)
                .apply(pageable);

        // ResponseDto 생성 및 데이터 설정
        return PageableDto.PageableResponseDto
                .<PendingUserGroupDto.PendingUserGroupResponseDto>builder()
                .data(pendingUserGroupsPage
                        .stream()
                        .map(PendingUserGroup::toDto)
                        .toList())
                .sortBy(sortBy)
                .pageSize(pendingUserGroupsPage.getSize())
                .page(pendingUserGroupsPage.getNumber())
                .hasNext(pendingUserGroupsPage.hasNext())
                .hasPrevious(pendingUserGroupsPage.hasPrevious())
                .build();
    }

    // 그룹 투표
    public void voteGroup(String userId, String groupId) {
        LOGGER.info("[UserGroupService] - voteGroup : userId={}, groupId={}", userId, groupId);
        // 유효하지 않은 그룹 id 투표 금지
        if (!pendingUserGroupRepository.existsById(groupId)) {
            LOGGER.error("[UserGroupService] - voteGroup failed : invalid group");
            throw new CustomException("INVALID GROUP.", HttpStatus.BAD_REQUEST);
        }

        // 최신 투표 내역 조회
        Optional<PendingUserGroupVote> latestVoteOpt = pendingUserGroupVoteRepository.findFirstByUserIdOrderByCreatedAtDesc(userId);

        // 이미 투표한 그룹 & 1시간 이내인지 확인
        if (latestVoteOpt.isPresent()) {
            PendingUserGroupVote latestVote = latestVoteOpt.get();
            if (!latestVote
                    .getCreatedAt()
                    .isBefore(LocalDateTime
                            .now()
                            .minusHours(1))) {
                LOGGER.error("[UserGroupService] - voteGroup failed : already voted within 1 hour");
                throw new CustomException("ALREADY VOTED GROUP.", HttpStatus.BAD_REQUEST);
            }
        }

        // 투표 저장
        PendingUserGroupVote pendingUserGroupVote = PendingUserGroupVote
                .builder()
                .userId(userId)
                .pendingUserGroupId(groupId)
                .createdAt(LocalDateTime.now())
                .build();
        pendingUserGroupVoteRepository.save(pendingUserGroupVote);

        // 투표수 증가
        PendingUserGroup pendingUserGroup = pendingUserGroupRepository
                .findById(groupId)
                .orElseThrow(() -> new CustomException("INVALID GROUP.", HttpStatus.BAD_REQUEST));

        pendingUserGroup.setVoteCount(pendingUserGroup.getVoteCount() + 1);
        pendingUserGroupRepository.save(pendingUserGroup);

        LOGGER.info("[UserGroupService] - voteGroup success : {}", pendingUserGroupVote);
    }


    // 그룹 생성 신청
    public void createGroup(User user, PendingUserGroupDto.PendingUserGroupRequestDto pendingUserGroupRequestDto) {
        LOGGER.info("[UserGroupService] - createGroup : {}", user.getId());
        // 이미 생성한 그룹인 경우
        Optional<PendingUserGroup> latestGroupOpt = pendingUserGroupRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId());
        LocalDateTime limitTime = LocalDateTime
                .now()
                .minusHours(6);
        boolean canCreateGroup = latestGroupOpt
                .map(pendingUserGroup -> pendingUserGroup
                        .getCreatedAt()
                        .isBefore(limitTime))
                .orElse(true); // 값이 없으면 그룹 생성 가능
        if (!canCreateGroup) {
            LOGGER.error("[UserGroupService] - createGroup failed : already created group within 6 hours");
            throw new CustomException("ALREADY CREATED GROUP.", HttpStatus.BAD_REQUEST);
        }


        // 그룹 생성 신청
        PendingUserGroup pendingUserGroup = new PendingUserGroup();
        pendingUserGroup.setUserId(user.getId());
        pendingUserGroup.setName(pendingUserGroupRequestDto.getName());
        pendingUserGroup.setDescription(pendingUserGroupRequestDto.getDescription());
        pendingUserGroup.setVoteCount(0);
        pendingUserGroup.setCreatedAt(LocalDateTime.now());
        pendingUserGroupRepository.save(pendingUserGroup);
        LOGGER.info("[UserGroupService] - createGroupVote success : {}", pendingUserGroup);
    }


    //    @Timed(value = "getPuuidList", description = "Get puuid list by group id")
    public List<String> getPuuidList(String groupId) {
        LOGGER.info("[UserGroupService] - getPuuidList : {}", groupId);
        List<UserGroupAssociation> userGroupAssociations = userGroupAssociationRepository.findAllByGroupId(groupId);
        List<String> puuidList = new ArrayList<>();
        userGroupAssociations.forEach(userGroupAssociation -> {
            Optional<UserPuuid> userPuuid = userPuuidRepository.findByUserId(userGroupAssociation.getUserId());
            userPuuid.ifPresent(puuid -> puuidList.add(puuid.getPuuid()));
        });
        return puuidList;
    }

    public Integer getGroupUserCount(String groupId) {
        LOGGER.info("[UserGroupService] - getGroupUserCount : {}", groupId);
        return userGroupAssociationRepository.countAllByGroupId(groupId);
    }
}
