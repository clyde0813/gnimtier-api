//package com.gnimtier.api.controller.auth;
//
//import com.gnimtier.api.config.security.JwtFilter;
//import com.gnimtier.api.config.security.JwtUtil;
//import com.gnimtier.api.exception.CustomException;
//import com.gnimtier.api.service.auth.AuthService;
//import org.apache.catalina.security.SecurityConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Map;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AuthControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private AuthController authController;
//
//    @MockitoBean
//    private AuthService authService;
//
//    @MockitoBean
//    private JwtFilter jwtFilter;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Test
//    @DisplayName("AuthController - refresh with valid refresh token")
//    void refreshWithValidRefreshToken() throws Exception {
//        String refreshToken = jwtUtil.generateRefreshToken("userId");
//        Map<String, String> tokens = Map.of("access_token", "newAccessToken", "refresh_token", "newRefreshToken");
//
//        when(authService.refreshToken(refreshToken)).thenReturn(tokens);
//
//        mockMvc.perform(get("/auth/refresh")
//                        .header("refresh", refreshToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.tokens.access_token").value("newAccessToken"))
//                .andExpect(jsonPath("$.data.tokens.refresh_token").value("newRefreshToken"));
//    }
//
//    @Test
//    @DisplayName("AuthController - refresh with missing refresh token")
//    void refreshWithMissingRefreshToken() throws Exception {
//        mockMvc.perform(get("/auth/refresh")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    @DisplayName("AuthController - refresh with invalid refresh token")
//    void refreshWithInvalidRefreshToken() throws Exception {
//        String refreshToken = "invalidRefreshToken";
//
//        when(authService.refreshToken(refreshToken)).thenThrow(new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED));
//
//        mockMvc.perform(get("/auth/refresh")
//                        .header("refresh", refreshToken)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Invalid refresh token"));
//    }
//}