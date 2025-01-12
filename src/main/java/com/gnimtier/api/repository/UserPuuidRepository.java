package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.riot.UserPuuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPuuidRepository extends JpaRepository<UserPuuid, Long> {
    UserPuuid findByPuuid(String puuid);
    UserPuuid findByUserId(String userId);
}
