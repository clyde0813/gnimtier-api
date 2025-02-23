package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.riot.UserPuuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPuuidRepository extends JpaRepository<UserPuuid, Long> {
    Optional<UserPuuid> findByPuuid(String puuid);
    Optional<UserPuuid> findByUserId(String userId);

    boolean existsByUserId(String id);
    boolean existsByPuuid(String puuid);
}
