package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.service.gnt.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class UserGroupController {
    private final UserGroupService userGroupService;

    @GetMapping("/by-parentId")
    public DataDto<Map<String, List<UserGroupDto>>> getGroups(
            @RequestParam(required = false) String parentId
    ) {
        return new DataDto<>(Map.of("groups", userGroupService.getUserGroupsByParentId(parentId)));
    }
}
