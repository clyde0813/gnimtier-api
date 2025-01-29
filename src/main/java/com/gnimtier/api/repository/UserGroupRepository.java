package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.gnt.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
    UserGroup findByName(String name);

    UserGroup findById(String id);
}
