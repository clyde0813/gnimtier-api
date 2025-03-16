package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.basic.StatusDto;
import com.gnimtier.api.data.dto.gnt.UserCommentDto;
import com.gnimtier.api.data.dto.riot.client.PageableDto;
import com.gnimtier.api.data.dto.riot.internal.request.SummonerRegisterRequestDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserCommentService;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import com.gnimtier.api.service.gnt.UserTmpService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final AuthService authService;
    private final UserService userService;
    private final UserTmpService userTmpService;
    private final UserGroupService userGroupService;
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserCommentService userCommentService;

    // 사용자 정보 조회
    // json : data - user - UserDto
    @Tag(name = "(User) 사용자 정보 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId) {
        LOGGER.info("[UserController.getUser()] called");
        return ResponseEntity.ok(new DataDto<>(Map.of("user", userService
                .getUserByUserId(userId)
                .getUserDto())));
    }

    // 내 정보 조희
    // json : data - user - UserDto
    @Tag(name = "(User) 내 정보 조회", description = "로그인 필수")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMe() {
        LOGGER.info("[UserController.getMe()] called");
        User user = authService.getUserFromAuthentication();
        return ResponseEntity.ok(new DataDto<>(Map.of("user", user.getUserDto())));
    }

    // 가입된 그룹 조회
    // json : data - groups - List<UserGroupRankDto>
    @Tag(name = "(User) 사용자 그룹 조회")
    @GetMapping("/{userId}/groups")
    public ResponseEntity<?> getGroups(@PathVariable String userId) {
        LOGGER.info("[UserGroupController.getGroups()] Get groups");
        return ResponseEntity.ok(new DataDto<>(Map.of("groups", userGroupService.getUserGroups(userId))));
    }

    // 사용자 라이엇 계정 조회
    // json : data - summoner - Map<String, SummonerResponseDto>
    @Tag(name = "(User) 사용자 라이엇 계정 조회")
    @GetMapping("/{userId}/riot/summoners")
    public DataDto<?> getRiotAccount(@PathVariable String userId) {
        LOGGER.info("[UserController.getRiotAccount()] called");
        return new DataDto<>(Map.of("summoners", userService.getRiotAccount(userId)));
    }

    // 라이엇 계정 등록
    @Tag(name = "(User) 라이엇 계정 등록", description = "로그인 필수")
    @PostMapping("/riot/summoners")
    @PreAuthorize("isAuthenticated()")
    public StatusDto registerRiotAccount(@RequestBody(required = true) SummonerRegisterRequestDto summonerRegisterRequestDto) {
        LOGGER.info("[UserController.registerRiotAccount()] called");
        User user = authService.getUserFromAuthentication();
        userService.registerRiotAccount(user, summonerRegisterRequestDto.getPuuid());
        return new StatusDto(HttpStatus.ACCEPTED, "Riot account registered", LocalDateTime.now());
    }

    // (ONLY FOR TESTING) 계정 데이터 삭제
    @Tag(name = "(User) 계정 데이터 삭제", description = "로그인 필수")
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public StatusDto deleteAccount() {
        LOGGER.info("[UserController.deleteAccount()] called");
        User user = authService.getUserFromAuthentication();
        userTmpService.deleteAccount(user);
        return new StatusDto(HttpStatus.ACCEPTED, "Account deleted", LocalDateTime.now());
    }

    // 댓긇 조회
    @Tag(name = "(Comment) 사용자 댓글 조회", description = "댓글 조회")
    @GetMapping("/{userId}/comments")
    public DataDto<?> getUserComment(@PathVariable String userId, PageableDto.PlainRequestDto plainRequestDto) {
        LOGGER.info("[UserController.getUserComments] called");
        return new DataDto<>(userCommentService.getUserComment(userId, plainRequestDto));
    }

    // 댓글 등록
    @Tag(name = "(Comment) 사용자 댓글 등록", description = "댓글 등록")
    @PostMapping("/{userId}/comments")
    @PreAuthorize("isAuthenticated()")
    public StatusDto postUserComment(@PathVariable String userId, @RequestBody(required = true) UserCommentDto.UserCommentRequestDto userCommentRequestDto) {
        LOGGER.info("[UserController.postUserComment] called()");
        User user = authService.getUserFromAuthentication();
        userCommentService.postUserComment(user, userId, userCommentRequestDto);
        return new StatusDto(HttpStatus.ACCEPTED, "COMMENT HAS BEEN REGISTERED", LocalDateTime.now());
    }
}
