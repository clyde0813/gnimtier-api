package com.gnimtier.api.config.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
        // Token 분리하는 method 분리 가능함
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Bearer -> 공백 포함 7글자 제외하고 토큰 가져오기
            String token = authHeader.substring(7);
            try {
                LOGGER.info("[getTokenPayload] Token : {}", token);
                Jws<Claims> claimsJws = Jwts
                        .parser()
                        .verifyWith(jwtUtil.getSecretKey())
                        .build()
                        .parseSignedClaims(token);
                LOGGER.info("[getTokenPayload] Token payload : {}", claimsJws.getPayload());
                Claims claims = claimsJws.getPayload();
                String userId = claims.getSubject();
                if (userId != null) {
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(new JwtAuthentication(claims.getSubject()));
                }
            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response
                        .getWriter()
                        .write("Token Expired");
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response
                        .getWriter()
                        .write("Unauthorized");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
