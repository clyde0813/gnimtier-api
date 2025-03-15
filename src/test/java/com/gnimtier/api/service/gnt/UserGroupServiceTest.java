package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserGroupDto;
import com.gnimtier.api.data.dto.gnt.UserGroupRankDto;
import com.gnimtier.api.data.entity.gnt.UserGroup;
import com.gnimtier.api.data.entity.gnt.UserGroupAssociation;
import com.gnimtier.api.repository.UserGroupAssociationRepository;
import com.gnimtier.api.repository.UserGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 그룹 서비스 단위 테스트")
class UserGroupServiceTest {

    @Mock
    private UserGroupAssociationRepository userGroupAssociationRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    @InjectMocks
    private UserGroupService userGroupService;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @DisplayName("유저 그룹 조회 - by parent id")
    void getUserGroupsByParentId_returnsUserGroups() {
        String parentId = "parent1";
        UserGroup userGroup = new UserGroup();
        userGroup.setId("group1");
        userGroup.setName("Group 1");
        userGroup.setParentId(parentId);
        when(userGroupRepository.findAllByParentId(parentId)).thenReturn(List.of(userGroup));

        List<UserGroupDto> result = userGroupService.getUserGroupsByParentId(parentId);

        assertEquals(1, result.size());
        assertEquals("group1", result.getFirst().getId());
    }

    @Test
    @DisplayName("유저 그룹 조회 - by parent id - no groups")
    void getUserGroupsByParentId_returnsEmptyListWhenNoGroups() {
        String parentId = "parent1";
        when(userGroupRepository.findAllByParentId(parentId)).thenReturn(Collections.emptyList());

        List<UserGroupDto> result = userGroupService.getUserGroupsByParentId(parentId);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("유저 그룹 조회 - by null parent id")
    void getUserGroupsByParentId_returnsEmptyListWhenParentIdIsNull() {
        List<UserGroupDto> result = userGroupService.getUserGroupsByParentId(null);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("유저 그룹 회 - 그룹 가입 O")
    void getUserGroups_returnsUserGroups() {
        String userId = "user1";
        UserGroup userGroup = new UserGroup();
        userGroup.setId("group1");
        userGroup.setName("Group 1");
        userGroup.setDescription("group 1");
        userGroup.setCreatedAt(LocalDateTime.now());
        userGroup.setOfficial(true);
        userGroup.setParentId(null);
        when(userGroupAssociationRepository.findAllByUserId(userId)).thenReturn(List.of(new UserGroupAssociation(1L, userId, "group1", "group1")));
        when(userGroupRepository.findById("group1")).thenReturn(userGroup);

        List<UserGroupRankDto> result = userGroupService.getUserGroups(userId);

        assertEquals(1, result.size());
        assertEquals("group1", result.getFirst().getId());
    }

    @Test
    @DisplayName("유저 그룹 조회 - 그룹 가입 X")
    void getUserGroups_returnsEmptyMapWhenNoAssociations() {
        String userId = "user1";
        when(userGroupAssociationRepository.findAllByUserId(userId)).thenReturn(Collections.emptyList());

        List<UserGroupRankDto> result = userGroupService.getUserGroups(userId);

        assertEquals(0, result.size());
    }
}