package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupAssociationRepository extends JpaRepository<UserGroupAssociation, Long> {
    List<UserGroupAssociation> findAllByUserId(String userId);

    List<UserGroupAssociation> findAllByGroupId(String groupId);
}
