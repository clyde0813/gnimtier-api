package com.gnimtier.api.service.gnt;

import com.gnimtier.api.client.riot.RiotApiClient;
import com.gnimtier.api.data.dto.gnt.PendingUserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupRankDto;
import com.gnimtier.api.data.dto.riot.client.Response.PageableResponseDto;
import com.gnimtier.api.data.dto.riot.client.request.RankRequestDto;
import com.gnimtier.api.data.dto.riot.internal.request.UserGroupSearchRequestDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.PendingUserGroup;
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
        // 가입한 그룹이 없는 경우
        if (userGroupAssociationRepository
                .findAllByUserId(userId)
                .isEmpty()) {
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
        return userGroups;
    }


    // 투표중인 그룹 검색
    public PageableResponseDto<PendingUserGroupDto> getPendingGroups(String sortBy, int page) {
        int PAGE_SIZE = 5;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        // 정렬 조건에 따라 Repository 메서드 매핑
        Map<String, Function<Pageable, Page<PendingUserGroup>>> sortMethodMap = Map.of(
                "votes", pendingUserGroupRepository::findAllByOrderByVoteCountDesc,
                "createdAt", pendingUserGroupRepository::findAllByOrderByCreatedAtDesc,
                "name", pendingUserGroupRepository::findAllByOrderByNameAsc
        );

        // 존재하지 않는 sortBy일 경우 기본 정렬 적용
        Page<PendingUserGroup> pendingUserGroupsPage =
                sortMethodMap.getOrDefault(sortBy, pendingUserGroupRepository::findAllByOrderByVoteCountDesc)
                        .apply(pageable);

        // ResponseDto 생성 및 데이터 설정
        return PageableResponseDto.<PendingUserGroupDto>builder()
                .data(pendingUserGroupsPage.stream().map(PendingUserGroup::toDto).toList())
                .sortBy(sortBy)
                .pageSize(pendingUserGroupsPage.getSize())
                .page(pendingUserGroupsPage.getNumber())
                .hasNext(pendingUserGroupsPage.hasNext())
                .hasPrevious(pendingUserGroupsPage.hasPrevious())
                .build();
    }


    // 그룹 가입
    public void joinGroup(User user, String groupId) {
        // 중복 가입 금지
        if (userGroupAssociationRepository.existsUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - joinGroup failed : already joined group");
            throw new CustomException("Already joined group.", HttpStatus.BAD_REQUEST);
        }

        // 유효하지 않은 그룹 id 가입 금지
        if (!userGroupRepository.existsUserGroupById(groupId)) {
            throw new CustomException("Invalid group.", HttpStatus.BAD_REQUEST);
        }

        // isJoinable false 그룹 가입 금지
        if (userGroupRepository.existsUserGroupById(groupId) && !userGroupRepository
                .findById(groupId)
                .isJoinable()) {
            throw new CustomException("Invalid group.", HttpStatus.BAD_REQUEST);
        }

        UserGroupAssociation userGroupAssociation = new UserGroupAssociation();
        userGroupAssociation.setUserId(user.getId());
        userGroupAssociation.setGroupId(groupId);
        userGroupAssociationRepository.save(userGroupAssociation);
        LOGGER.info("[UserGroupService] - joinGroup success : {}", userGroupAssociation);
    }

    @Transactional
    public void leaveGroup(User user, String groupId) {
        // 가입되지 않은 그룹 탈퇴 불가
        if (!userGroupAssociationRepository.existsUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - leaveGroup failed : not joined group");
            throw new CustomException("Not joined group.", HttpStatus.BAD_REQUEST);
        }

        userGroupAssociationRepository.deleteUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId);
        LOGGER.info("[UserGroupService] - leaveGroup success : {}", user.getId());
    }

    public List<String> getPuuidList(String groupId) {
        List<UserGroupAssociation> userGroupAssociations = userGroupAssociationRepository.findAllByGroupId(groupId);
        List<String> puuidList = new ArrayList<>();
        userGroupAssociations.forEach(userGroupAssociation -> {
            Optional<UserPuuid> userPuuid = userPuuidRepository.findByUserId(userGroupAssociation.getUserId());
            userPuuid.ifPresent(puuid -> puuidList.add(puuid.getPuuid()));
        });
        return puuidList;
    }

    public Integer getGroupUserCount(String groupId) {
        return userGroupAssociationRepository.countAllByGroupId(groupId);
    }
}
