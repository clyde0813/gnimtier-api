package com.gnimtier.api.service.auth;

import com.gnimtier.api.config.security.JwtAuthentication;
import com.gnimtier.api.config.security.JwtUtil;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.gnt.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

//    public Boolean signUp(SignUpDto signUpDto) {
//        String encodedPassword = passwordEncoderService.encode(signUpDto.getPassword());
//        User user = new User();
//        user.setUsername(signUpDto.getUsername());
//        user.setPassword(encodedPassword);
//        userRepository.save(user);
//        return true;
//    }

//    public Map<String, String> login(LoginDto loginDto) {
//        String username = loginDto.getUsername();
//        String password = loginDto.getPassword();
//        Optional<User> user = userRepository.findByUsername(username);
//        if (user.isEmpty() || !passwordEncoderService.matches(password, user
//                .get()
//                .getPassword())) {
//            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
//        }
//        String accessToken = jwtUtil.generateAccessToken(user
//                .get()
//                .getId());
//        String refreshToken = jwtUtil.generateRefreshToken(user
//                .get()
//                .getId());
//        return Map.of("access_token", accessToken, "refresh_token", refreshToken);
//    }

    public Map<String, String> refreshToken(String refreshToken) {
        try {
            // refresh token - Bearer 제거후 payload 확인
            Claims claims = jwtUtil.getTokenPayload(jwtUtil.resolveToken(refreshToken));
            if (!"refresh".equals(claims.get("tokenType"))) {
                LOGGER.error("[getUserFromToken] Token tokenType mismatch");
                throw new CustomException("tokenType Error", HttpStatus.BAD_REQUEST);
            }
            String newAccessToken = jwtUtil.generateAccessToken(claims.getSubject());
            String newRefreshToken = jwtUtil.generateRefreshToken(claims.getSubject());
            return Map.of("access_token", newAccessToken, "refresh_token", newRefreshToken);
        } catch (Exception e) {
            throw new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }
    }

    // 로그인(토큰 유효성) 검사
    public String isTokenValid(String token) {
        return jwtUtil.validateToken(token);
    }

    public User getUserFromAuthentication() {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) SecurityContextHolder
                .getContext()
                .getAuthentication();
        String userId = jwtAuthentication.getPrincipal();
        return userService.getUserByUserId(userId);
    }
}
