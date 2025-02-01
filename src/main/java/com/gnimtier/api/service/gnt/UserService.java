package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUserId(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
