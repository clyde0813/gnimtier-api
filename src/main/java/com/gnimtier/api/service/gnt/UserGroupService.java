package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import com.gnimtier.api.repository.UserPuuidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupAssociationRepository userGroupAssociationRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserPuuidRepository userPuuidRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(UserGroupService.class);

    public List<UserGroupDto> getUserGroupsByParentId(String parentId) {
        List<UserGroup> userGroupList = new ArrayList<>(userGroupRepository.findAllByParentId(parentId));
        LOGGER.info("UserGroupService: getUserGroupsByParentId: {}", userGroupList);
        List<UserGroupDto> userGroupDtoList = new ArrayList<>(userGroupList.size());
        userGroupList.forEach(userGroup -> userGroupDtoList.add(userGroup.toDto()));
        return userGroupDtoList;
    }

    public List<UserGroupDto> getUserGroups(String userId) {
        List<UserGroupDto> userGroups = new ArrayList<>();
        if (userGroupAssociationRepository
                .findAllByUserId(userId)
                .isEmpty()) {
            return new ArrayList<>();
        }
        userGroupAssociationRepository
                .findAllByUserId(userId)
                .forEach(userGroupAssociation -> {
                    String groupId = userGroupAssociation.getGroupId();
                    UserGroup userGroup = userGroupRepository.findById(groupId);
                    userGroups.add(userGroup.toDto());
                });
        return userGroups;
    }

    // 그룹 가입
    public void joinGroup(User user, String groupId) {
        String puuid = userPuuidRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new CustomException("등록된 Riot 계정이 없습니다.", HttpStatus.BAD_REQUEST))
                .getPuuid();

        // 중복 가입 금지
        if (userGroupAssociationRepository
                .existsUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - joinGroup failed : already joined group");
            throw new CustomException("이미 가입된 그룹입니다.", HttpStatus.BAD_REQUEST);
        }

        // 유효하지 않은 그룹 id 가입 금지
        if (!userGroupRepository.existsUserGroupById(groupId)) {
            throw new CustomException("유효하지 않은 그룹입니다.", HttpStatus.BAD_REQUEST);
        }

        // isRoot false 그룹 가입 금지
        if (userGroupRepository.existsUserGroupById(groupId) && !userGroupRepository
                .findById(groupId)
                .isRoot()) {
            throw new CustomException("유효하지 않은 그룹입니다.", HttpStatus.BAD_REQUEST);
        }

        UserGroupAssociation userGroupAssociation = new UserGroupAssociation();
        userGroupAssociation.setUserId(user.getId());
        userGroupAssociation.setGroupId(groupId);
        userGroupAssociation.setPuuid(puuid);
        userGroupAssociationRepository.save(userGroupAssociation);
        LOGGER.info("[UserGroupService] - joinGroup success : {}", userGroupAssociation);
    }

    @Transactional
    public void leaveGroup(User user, String groupId) {
        // 가입되지 않은 그룹 탈퇴 불가
        if (!userGroupAssociationRepository
                .existsUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId)) {
            LOGGER.error("[UserGroupService] - leaveGroup failed : not joined group");
            throw new CustomException("가입되지 않은 그룹입니다.", HttpStatus.BAD_REQUEST);
        }

        userGroupAssociationRepository
                .deleteUserGroupAssociationByUserIdAndGroupId(user.getId(), groupId);
        LOGGER.info("[UserGroupService] - leaveGroup success : {}", user.getId());
    }
}
