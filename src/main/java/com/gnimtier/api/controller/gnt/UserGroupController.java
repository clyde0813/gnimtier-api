package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.basic.StatusDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.riot.internal.request.UserGroupSearchRequestDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
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
    @PostMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public StatusDto joinGroup(@PathVariable String groupId) {
        LOGGER.info("[UserGroupController.joinGroup()] Join group");
        User user = authService.getUserFromAuthentication();
        userGroupService.joinGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group joined", LocalDateTime.now());
    }

    // 그룹 탈퇴
    @DeleteMapping("/{groupId}")
    @PreAuthorize("isAuthenticated()")
    public StatusDto leaveGroup(@PathVariable String groupId) {
        LOGGER.info("[UserGroupController.leaveGroup()] Leave group");
        User user = authService.getUserFromAuthentication();
        userGroupService.leaveGroup(user, groupId);
        return new StatusDto(HttpStatus.ACCEPTED, "Group left", LocalDateTime.now());
    }

    // 부모 id로 조회
    @GetMapping("/by-parentId")
    public DataDto<Map<String, List<UserGroupDto>>> getGroups(@RequestParam(required = false) String parentId) {
        return new DataDto<>(Map.of("groups", userGroupService.getUserGroupsByParentId(parentId)));
    }

    @GetMapping("/pending")
    public DataDto<?> getPendingGroups(
            @RequestParam(required = false, defaultValue = "votes") String sortBy,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
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
//    // 그룹 생성 신청
//    @PostMapping("")
//    @PreAuthorize("isAuthenticated()")
//    public StatusDto createGroupVote(@RequestBody ) {
//
//    }
//
//    // 그룹 투표
//    @PostMapping("/{groupId}/votes")
//    public DataDto<UserGroupDto> voteGroup(@PathVariable String groupId) {
//        return new DataDto<>(userGroupService.voteUserGroup(groupId));
//    }
}
