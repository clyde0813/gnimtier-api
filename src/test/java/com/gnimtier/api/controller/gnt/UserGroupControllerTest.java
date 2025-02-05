package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.service.gnt.UserGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserGroupController.class)
class UserGroupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserGroupService userGroupService;

    private UserGroupDto userGroupDto;

    @BeforeEach
    void setUp() {
        userGroupDto = UserGroupDto
                .builder()
                .id("testId")
                .name("testName")
                .description("testDescription")
                .parentId("testParentId")
                .isRoot(true)
                .build();
    }

    @Test
    void getGroups_WithNullParentId_ShouldReturnUserGroupDtos() throws Exception {
        when(userGroupService.getUserGroupsByParentId(null)).thenReturn(List.of(userGroupDto));

        mockMvc
                .perform(get("/groups/by-parentId").param("parentId", (String) null))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userGroupDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userGroupDto.getName()));
    }

    @Test
    void getGroups_WithEmptyParentId_ShouldReturnAllUserGroupDtos() throws Exception {
        when(userGroupService.getUserGroupsByParentId("")).thenReturn(List.of(userGroupDto));

        mockMvc
                .perform(get("/groups/by-parentId").param("parentId", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userGroupDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userGroupDto.getName()));
    }

    @Test
    void getGroups_WithInvalidParentId_ShouldReturnEmptyList() throws Exception {
        when(userGroupService.getUserGroupsByParentId("invalidParent")).thenReturn(Collections.emptyList());

        mockMvc
                .perform(get("/groups/by-parentId").param("parentId", "invalidParent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}