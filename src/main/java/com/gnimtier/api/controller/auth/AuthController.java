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
            @RequestHeader(value = "Refresh", required = false) String refreshToken
    ) {
        String bearerToken = refreshToken;
        return ResponseEntity.ok(authService.refreshToken(bearerToken));
    }

    // 토큰 검증
    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(
            @RequestParam String token
    ) {
        jwtUtil.validateToken(token);
        return ResponseEntity.ok(true);
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
