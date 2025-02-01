package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.config.security.JwtAuthentication;
import com.gnimtier.api.data.dto.gnt.UserDto;
import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final UserGroupService userGroupService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserRepository userRepository;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getMe() {
        LOGGER.info("[UserController.getMe()] called");
        User user = authService.getUserFromAuthentication();
        return ResponseEntity.ok(user.getUserDto());
    }

    @GetMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, List<UserGroupResponseDto>>> getGroups() {
        LOGGER.info("[UserGroupController.getGroups()] Get groups");
        User user = authService.getUserFromAuthentication();
        return ResponseEntity.ok(userGroupService.getUserGroups(user.getId()));
    }
}
