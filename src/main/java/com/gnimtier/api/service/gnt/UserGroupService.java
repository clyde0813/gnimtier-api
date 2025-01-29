package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
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

    public Map<String, List<UserGroupResponseDto>> getUserGroups(String userId) {
        Map<String, List<UserGroupResponseDto>> userGroups = new HashMap<>();
        userGroups.put("groups", new ArrayList<>());
        if (userGroupAssociationRepository
                .findAllByUserId(userId)
                .isEmpty()) {
            return Map.of("groups", new ArrayList<>());
        }
        userGroupAssociationRepository
                .findAllByUserId(userId)
                .forEach(userGroupAssociation -> {
                    String groupId = userGroupAssociation.getGroupId();
                    UserGroup userGroup = userGroupRepository.findById(groupId);
                    UserGroupResponseDto userGroupResponseDto = new UserGroupResponseDto();
                    userGroupResponseDto.setId(userGroup.getId());
                    userGroupResponseDto.setName(userGroup.getName());
                    userGroupResponseDto.setDescription(userGroup.getDescription());
                    userGroups
                            .get("groups")
                            .add(userGroupResponseDto);
                });
        return userGroups;
    }
}
