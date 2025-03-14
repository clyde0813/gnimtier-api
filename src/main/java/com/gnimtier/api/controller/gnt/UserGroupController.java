package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.basic.StatusDto;
import com.gnimtier.api.data.dto.gnt.PendingUserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/groups")
public class UserGroupController {
    private final AuthService authService;
    private final UserGroupService userGroupService;

    private final Logger LOGGER = LoggerFactory.getLogger(UserGroupController.class);

    // 그룹 가입
    @Tag(name = "(UserGroup) 그룹 가입", description = "로그인 필수")
    @PostMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public StatusDto joinGroup(@PathVariable String groupId) {
        LOGGER.info("[UserGroupController.joinGroup()] Join group");
        User user = authService.getUserFromAuthentication();
        userGroupService.joinGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group joined", LocalDateTime.now());
    }

    // 그룹 탈퇴
    @Tag(name = "(UserGroup) 그룹 탈퇴", description = "로그인 필수")
    @DeleteMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public StatusDto leaveGroup(@PathVariable String groupId) {
        LOGGER.info("[UserGroupController.leaveGroup()] Leave group");
        User user = authService.getUserFromAuthentication();
        userGroupService.leaveGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group left", LocalDateTime.now());
    }

    // 부모 id로 조회
    @Tag(name = "(UserGroup) 부모 id로 그룹 조회", description = "부모 id로 하위 그룹 조회")
    @GetMapping("/by-parentId")
    public DataDto<Map<String, List<UserGroupDto>>> getGroups(@RequestParam(required = false) String parentId) {
        return new DataDto<>(Map.of("groups", userGroupService.getUserGroupsByParentId(parentId)));
    }

    @Tag(name = "(UserGroup) 투표중인 그룹 조회", description = "sortBy votes, createdAt, name")
    @GetMapping("/pending")
    public DataDto<?> getPendingGroups(@RequestParam(required = false, defaultValue = "votes") String sortBy, @RequestParam(required = false, defaultValue = "0") int page) {
        return new DataDto<>(Map.of("groups", userGroupService.getPendingGroups(sortBy, page)));
    }

    //    // 그룹 상세 정보
//    @GetMapping("/{groupId}")
//    public DataDto<UserGroupDto> getGroup(@PathVariable String groupId) {
//        return new DataDto<>(userGroupService.getUserGroup(groupId));
//    }
//
    // 그룹 전체 조회
//    @GetMapping("")
//    public DataDto<?> getGroups(UserGroupSearchRequestDto userGroupSearchRequestDto) {
//        return new DataDto<>(Map.of("groups", userGroupService.getUserGroups(userGroupSearchRequestDto)));
//    }
//
    // 그룹 생성 신청
    @Tag(name = "(UserGroup) 그룹 생성 신청", description = "로그인 필수, 6시간당 1개의 그룹 생성 신청 가능")
    @PostMapping("/pending")
    @PreAuthorize("isAuthenticated()")
    public StatusDto createGroupVote(@RequestBody PendingUserGroupDto.PendingUserGroupRequestDto pendingUserGroupRequestDto) {
        User user = authService.getUserFromAuthentication();
        userGroupService.createGroup(user, pendingUserGroupRequestDto);
        return new StatusDto(HttpStatus.ACCEPTED, "UserGroup created", LocalDateTime.now());
    }

    // 그룹 투표
    @Tag(name = "(UserGroup) 그룹 투표", description = "로그인 필수, 1시간당 1개의 그룹 투표 가능")
    @PostMapping("/pending/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public StatusDto voteGroup(@PathVariable String groupId) {
        User user = authService.getUserFromAuthentication();
        userGroupService.voteGroup(user.getId(), groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group voted", LocalDateTime.now());
    }
}
