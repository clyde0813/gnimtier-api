package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.auth.UserOauth;
import com.gnimtier.api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTmpService {
    private final PendingUserGroupRepository pendingUserGroupRepository;
    private final PendingUserGroupVoteRepository pendingUserGroupVoteRepository;
    private final UserRepository userRepository;
    private final UserGroupAssociationRepository userGroupAssociationRepository;
    private final UserOauthRepository userOauthRepository;
    private final UserPuuidRepository userPuuidRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(UserTmpService.class);

    @Transactional
    public void deleteAccount(User user) {
        LOGGER.info("[deleteAccount] - Deleting Account");
        String userId = user.getId();
        userRepository.delete(user);
        userPuuidRepository.deleteAllByUserId(userId);
        userOauthRepository.deleteAllByUserId(userId);
        userGroupAssociationRepository.deleteAllByUserId(userId);
        pendingUserGroupVoteRepository.deleteAllByUserId(userId);
        pendingUserGroupRepository.deleteAllByUserId(userId);
    }
}
