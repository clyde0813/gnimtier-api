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

    @GetMapping("/riot/account")
    @PreAuthorize("isAuthenticated()")
    public DataDto<Map<String, SummonerResponseDto>> getRiotAccount() {
        LOGGER.info("[UserController.getRiotAccount()] called");
        User user = authService.getUserFromAuthentication();
        return new DataDto<>(Map.of("summoner", userService.getRiotAccount(user)));
    }

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
