package com.gnimtier.api.repository;

import com.gnimtier.api.data.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findById(String userId);

    Optional<User> findByUsername(String username);
}
