package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupAssociationRepository userGroupAssociationRepository;
    private final UserGroupRepository userGroupRepository;

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
}
