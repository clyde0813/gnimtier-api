package com.gnimtier.api.controller.gnt;

import com.gnimtier.api.data.dto.gnt.UserDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.auth.AuthService;
import com.gnimtier.api.service.gnt.UserGroupService;
import com.gnimtier.api.service.gnt.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// isAuthenticated 인하여 인증 관련 로직이 추가되어야함
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private UserGroupService userGroupService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("123");
        user.setNickname("testUser");
        user.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser
    void getMe_ShouldReturnUserDto() throws Exception {
        when(authService.getUserFromAuthentication()).thenReturn(user);
        when(user.getUserDto()).thenReturn(new UserDto(user.getId(), user.getNickname()));

        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.nickname").value(user.getNickname()));

        verify(authService, times(1)).getUserFromAuthentication();
    }

    @Test
    void getMe_Unauthorized_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }
}