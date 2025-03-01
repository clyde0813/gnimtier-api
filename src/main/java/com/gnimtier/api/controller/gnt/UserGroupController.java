package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.basic.StatusDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.service.gnt.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class UserGroupController {
    private final UserGroupService userGroupService;

    // 부모 id로 조회
    @GetMapping("/by-parentId")
    public DataDto<Map<String, List<UserGroupDto>>> getGroups(@RequestParam(required = false) String parentId) {
        return new DataDto<>(Map.of("groups", userGroupService.getUserGroupsByParentId(parentId)));
    }


//    // 그룹 상세 정보
//    @GetMapping("/{groupId}")
//    public DataDto<UserGroupDto> getGroup(@PathVariable String groupId) {
//        return new DataDto<>(userGroupService.getUserGroup(groupId));
//    }
//
//    // 그룹 전체 조회
//    @GetMapping("")
//    public DataDto<List<UserGroupDto>> getGroups(
//            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
//            @RequestParam(value = "keyword", required = false) String keyword,
//            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
//            @RequestParam(value = "isOfficial", required = false, defaultValue = "true") boolean isOfficial
//    ) {
//        return new DataDto<>(userGroupService.getUserGroups());
//    }
//
//    // 그룹 생성 신청
//    @PostMapping("")
//    @PreAuthorize("isAuthenticated()")
//    public StatusDto createGroupVote(@RequestBody UserGroupDto userGroupDto) {
//
//    }
//
//    // 그룹 투표
//    @PostMapping("/{groupId}/votes")
//    public DataDto<UserGroupDto> voteGroup(@PathVariable String groupId) {
//        return new DataDto<>(userGroupService.voteUserGroup(groupId));
//    }
}
