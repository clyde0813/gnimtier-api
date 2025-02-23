package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.DataDto;
import com.gnimtier.api.data.dto.gnt.UserDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
    private final UserGroupService userGroupService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public DataDto<UserDto> getMe() {
        LOGGER.info("[UserController.getMe()] called");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(user.getUserDto());
//        return ResponseEntity.ok(user.getUserDto());
    }

    @GetMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public DataDto<List<UserGroupDto>> getGroups() {
        LOGGER.info("[UserGroupController.getGroups()] Get groups");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(userGroupService.getUserGroups(user.getId()));
    }

//    @PostMapping("/riot/account")
//    @PreAuthorize("isAuthenticated()")
//    public DataDto<Map<String, SummonerResponseDto>> registerRiotAccount(
//
//    ) {
//        LOGGER.info("[UserController.registerRiotAccount()] called");
//        User user = authService.getUserFromAuthentication();
//        return new DataDto<>("success", null);
//    }
}
