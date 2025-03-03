package com.gnimtier.api.controller.auth;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    // json : data - tokens - Map<String, String>\
    @Tag(name = "(Auth) 토큰 Refresh", description = "로그인 필수")
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader(value = "refresh", required = false) String refreshToken) {
        return ResponseEntity.ok(new DataDto<>(Map.of("tokens", authService.refreshToken(refreshToken))));
    }
}
