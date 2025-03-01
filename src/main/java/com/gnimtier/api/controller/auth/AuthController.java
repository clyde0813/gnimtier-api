package com.gnimtier.api.controller.auth;

import com.gnimtier.api.config.security.JwtUtil;
import com.gnimtier.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // 토큰 Refresh
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestHeader(value = "refresh", required = false) String refreshToken
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
