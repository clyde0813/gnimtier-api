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

}
