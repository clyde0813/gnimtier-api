package com.gnimtier.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        //토큰 있을때만, 조건문 없으면 토큰 없으면 무조건 invalid token 에러
        try {
            String token = jwtUtil.resolveToken(request.getHeader("Authorization"));
            LOGGER.info("[getTokenPayload] Token : {}", token);
            Jws<Claims> claimsJws = Jwts
                    .parser()
                    .verifyWith(jwtUtil.getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            LOGGER.info("[getTokenPayload] Token payload : {}", claimsJws.getPayload());
            Claims claims = claimsJws.getPayload();
            if (!claims.get("tokenType").equals("access")) {
                handleException(response, "Invalid Token", HttpStatus.UNAUTHORIZED);
                return;
            }
            String userId = claims.getSubject();
            if (userId != null) {
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(new JwtAuthentication(claims.getSubject()));
            }
        } catch (ExpiredJwtException e) {
            handleException(response, "Token Expired", HttpStatus.UNAUTHORIZED);
            return;
        } catch (AuthorizationDeniedException e) {
            handleException(response, "Unauthorized", HttpStatus.UNAUTHORIZED);
            return;
        } catch (Exception e) {
            handleException(response, "Invalid Token", HttpStatus.UNAUTHORIZED);
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            handleException(response, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleException(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        // HTTP 상태 코드 설정
        response.setStatus(status.value());

        // 응답 헤더 설정 (JSON 형식 및 UTF-8 인코딩)
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // JSON 응답 데이터 생성
        Map<String, Object> errorDetails = Map.of(
                "timestamp", LocalDateTime
                        .now()
                        .toString(), // 현재 시간
                "message", message,                           // 에러 메시지
                "status", status.value()                     // HTTP 상태 코드
        );

        // JSON 변환기 (ObjectMapper) 사용하여 Map을 JSON 문자열로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorDetails);

        LOGGER.error("[JwtFilter Exception] : {}", jsonResponse);

        // JSON 응답을 HTTP 응답 바디에 작성
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(jsonResponse.getBytes(StandardCharsets.UTF_8)); // UTF-8 인코딩 유지
            out.flush(); // 출력 스트림 비우기
        }
    }
}
