package com.gnimtier.api.controller.auth;

import com.gnimtier.api.config.security.JwtUtil;
import com.gnimtier.api.service.auth.AuthService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // 토큰 Refresh
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestParam String refreshToken
    ) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    // 토큰 검증
    @GetMapping("/validate")
    public ResponseEntity<Claims> validate(
            @RequestParam String token
    ) {
        return ResponseEntity.ok(jwtUtil.validateToken(token));
    }

    // 회원가입
//    @PostMapping("/signup")
//    public ResponseEntity<?> register(@RequestBody SignUpDto signUpDto) {
//        authService.signUp(signUpDto);
//        return ResponseEntity.ok("User registered successfully");
//    }

    // 로그인
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
//        return ResponseEntity.ok(authService.login(loginDto));
//    }

//    // 토큰 - 사용자 추출
//    @GetMapping("/token")
//    public ResponseEntity<?> getUser(
//            @RequestParam String token
//    ) {
//        return ResponseEntity.ok(authService.getUserByAccessToken(token));
//    }
}
