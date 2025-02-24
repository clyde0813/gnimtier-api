package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.basic.StatusDto;
import com.gnimtier.api.data.dto.gnt.UserDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import com.gnimtier.api.service.riot.tft.SummonerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
    private final UserGroupService userGroupService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final SummonerService summonerService;

    // 내 정보 조희
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public DataDto<UserDto> getMe() {
        LOGGER.info("[UserController.getMe()] called");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(user.getUserDto());
    }

    // 가입된 그룹 조회
    @GetMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public DataDto<Map<String, List<UserGroupDto>>> getGroups() {
        LOGGER.info("[UserGroupController.getGroups()] Get groups");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(Map.of("groups", userGroupService.getUserGroups(user.getId())));
    }

    // 그룹 가입
    @PostMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public StatusDto joinGroup(
            @RequestParam(value = "groupId", required = true) String groupId
    ) {
        LOGGER.info("[UserGroupController.joinGroup()] Join group");
        User user = authService.getUserFromAuthentication();
        userGroupService.joinGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group joined", LocalDateTime.now());
    }

    // 그룹 탈퇴
    @DeleteMapping("/groups")
    @PreAuthorize("isAuthenticated()")
    public StatusDto leaveGroup(
            @RequestParam(value = "groupId", required = true) String groupId
    ) {
        LOGGER.info("[UserGroupController.leaveGroup()] Leave group");
        User user = authService.getUserFromAuthentication();
        userGroupService.leaveGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group left", LocalDateTime.now());
    }

    // 내 라이엇 계정 조회
    @GetMapping("/riot/account")
    @PreAuthorize("isAuthenticated()")
    public DataDto<Map<String, SummonerResponseDto>> getRiotAccount() {
        LOGGER.info("[UserController.getRiotAccount()] called");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(Map.of("summoner", userService.getRiotAccount(user)));
    }

    // 라이엇 계정 등록
    @PostMapping("/riot/account")
    @PreAuthorize("isAuthenticated()")
    public StatusDto registerRiotAccount(
            @RequestParam(value = "puuid", required = true) String puuid
    ) {
        LOGGER.info("[UserController.registerRiotAccount()] called");
        User user = authService.getUserFromAuthentication();
        userService.registerRiotAccount(user, puuid);
        return new StatusDto(HttpStatus.ACCEPTED, "Riot account registered", LocalDateTime.now());
    }
}
