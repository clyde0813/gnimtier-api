package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserGroupAssociationRepository extends JpaRepository<UserGroupAssociation, Long> {
    List<UserGroupAssociation> findAllByUserId(String userId);

    List<UserGroupAssociation> findAllByGroupId(String groupId);

    Optional<UserGroupAssociation> findByUserIdAndGroupId(String userId, String groupId);

    void deleteUserGroupAssociationByUserIdAndGroupId(String userId, String groupId);
    
    Boolean existsByUserIdAndGroupId(String userId, String groupId);

    Integer countAllByGroupId(String groupId);
}
