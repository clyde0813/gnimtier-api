package com.gnimtier.api.service.initializer;

import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import com.gnimtier.api.service.data.RedisService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGroupInitializer {
    private final RedisService redisService;
    private final UserGroupRepository userGroupRepository;
    private final UserGroupAssociationRepository userGroupAssociationRepository;

    @PostConstruct
    public void init() {
        Optional<List<UserGroup>> userGroups = userGroupRepository.findAllByRootIsTrue();
        if(userGroups.isEmpty()) {
            return;
        }
        userGroups.get().forEach(userGroup -> {
            userGroupAssociationRepository.findAllByGroupId(userGroup.getId()).forEach(userGroupAssociation -> {
                // 현재는 tft 밖에 없으니까 하드코딩
                String category = "tft";
                String key = category + ":user_group:" + userGroup.getId() + ":puuids";
                redisService.addToSet(key, userGroupAssociation.getPuuid());
            });
        });
    }
}
