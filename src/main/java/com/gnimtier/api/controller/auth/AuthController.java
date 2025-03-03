package com.gnimtier.api.controller.auth;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    // 토큰 Refresh
    // json : data - tokens - Map<String, String>
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = "refresh", required = false) String refreshToken) {
        return ResponseEntity.ok(new DataDto<>(Map.of("tokens", authService.refreshToken(refreshToken))));
    }
}
