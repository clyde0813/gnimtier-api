package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupResponseDto;
import com.gnimtier.api.service.gnt.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserGroupV1Controller {
    private final UserGroupService userGroupService;

    @Autowired
    public UserGroupV1Controller(UserGroupService userGroupService) {
        this.userGroupService = userGroupService;
    }

    @GetMapping("/{userId}/groups")
    public Map<String, List<UserGroupResponseDto>> getGroups(@PathVariable String userId) {
        return userGroupService.getUserGroups(userId);
    }
}
