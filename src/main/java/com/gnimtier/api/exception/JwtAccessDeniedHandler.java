package com.gnimtier.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnimtier.api.data.dto.basic.StatusDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 변환기

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.error("[JwtAccessDeniedHandler.handle()] Access Denied error: {}", accessDeniedException.getMessage());
        // JSON 응답 데이터 생성
        Map<String, Object> errorDetails = Map.of("timestamp", LocalDateTime
                        .now()
                        .toString(), // 현재 시간
                "message", "Unauthorized",                           // 에러 메시지
                "status", HttpStatus.UNAUTHORIZED.value()               // HTTP 상태 코드
        );
        String jsonResponse = objectMapper.writeValueAsString(errorDetails);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response
                .getOutputStream()
                .write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        response
                .getOutputStream()
                .flush();
    }
}