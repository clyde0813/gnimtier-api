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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

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
        } catch (CustomException e) {
            throw new CustomException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            throw new CustomException("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }
    }

    public User getUserFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthentication jwtAuthentication)) {
            throw new CustomException("Invalid authentication", HttpStatus.UNAUTHORIZED);
        }

        String userId = jwtAuthentication.getPrincipal();
        if (userId == null || userId.isEmpty()) {
            throw new CustomException("Invalid authentication", HttpStatus.UNAUTHORIZED);
        }

        User user = userService.getUserByUserId(userId);
        if (user == null) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }

        return user;
    }
}
