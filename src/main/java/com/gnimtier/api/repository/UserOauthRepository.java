package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.auth.UserOauth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOauthRepository extends JpaRepository<UserOauth, String> {
    Optional<UserOauth> findById(String id);

    // ONLY FOR TESTING
    void deleteAllByUserId(String userId);
}
