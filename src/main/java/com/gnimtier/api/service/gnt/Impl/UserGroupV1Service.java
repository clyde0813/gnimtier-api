package com.gnimtier.api.service.gnt.Impl;

import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.service.gnt.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserGroupV1Service implements UserGroupService {
    private final UserGroupAssociationRepository userGroupAssociationRepository;

    @Autowired
    public UserGroupV1Service(UserGroupAssociationRepository userGroupAssociationRepository) {
        this.userGroupAssociationRepository = userGroupAssociationRepository;
    }

    @Override
    public Map<String, List<UserGroupResponseDto>> getUserGroups(String userId) {
        Map<String, List<UserGroupResponseDto>> userGroups = new HashMap<>();
        userGroups.put("groups", new ArrayList<>());
        userGroupAssociationRepository.findAllByUserId(userId).forEach(userGroupAssociation -> {
            UserGroup userGroup = userGroupAssociation.getUserGroup();
            UserGroupResponseDto userGroupResponseDto = new UserGroupResponseDto();
            userGroupResponseDto.setId(userGroup.getId());
            userGroupResponseDto.setName(userGroup.getName());
            userGroupResponseDto.setDescription(userGroup.getDescription());
            userGroupResponseDto.setCategory(userGroup.getGroupCategory().getName());
            userGroups.get("groups").add(userGroupResponseDto);
        });
        return userGroups;
    }
}
