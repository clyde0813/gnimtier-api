package com.gnimtier.api.config.security;

import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private final SecretKey SECRET_KEY;
    private final long ACCESS_TOKEN_EXPIRATION_TIME;
    private final long REFRESH_TOKEN_EXPIRATION_TIME;
    private final UserRepository userRepository;

    //static 으로 만들고자 하였으나, @Value 어노테이션은 static 변수에 적용이 불가능함
    @Autowired
    public JwtUtil(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.access.expiration}") long accessExpiration,
            @Value("${jwt.refresh.expiration}") long refreshExpiration,
            UserRepository userRepository) {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.ACCESS_TOKEN_EXPIRATION_TIME = accessExpiration;
        this.REFRESH_TOKEN_EXPIRATION_TIME = refreshExpiration;
        this.userRepository = userRepository;
    }


    public static String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    // 접근 토큰 생성
    public String generateAccessToken(String userId) {
        LOGGER.info("[generateAccessToken] Access Token Generated : {}", userId);
        return Jwts
                .builder()
                .claim("tokenType", "access")
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 갱신 토큰 생성
    public String generateRefreshToken(String userId) {
        LOGGER.info("[generateRefreshToken] Refresh Token Generated : {}", userId);
        return Jwts
                .builder()
                .claim("tokenType", "refresh")
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 토큰 payload extract
    public Claims getTokenPayload(String token) {
        LOGGER.info("[getTokenPayload] Token");
        Jws<Claims> claimsJws = Jwts
                .parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
        LOGGER.info("[getTokenPayload] Token payload : {}", claimsJws.getPayload());
        return claimsJws.getPayload();
    }


    public User getUserFromToken(String token) {
        LOGGER.info("[getUserFromToken] Token get User start");
        Claims claims = getTokenPayload(token);
        if (!"access".equals(claims.get("tokenType"))) {
            LOGGER.error("[getUserFromToken] Token tokenType mismatch");
            throw new CustomException("tokenType Error", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findById((String) claims.getSubject());
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }

    public String validateToken(String token) {
        LOGGER.info("[validateToken] Token validation check start");
        try {
            getTokenPayload(token);
            LOGGER.info("[validateToken] Token validation check success");
            return token;
        } catch (Exception e) {
            LOGGER.error("[validateToken] Token validation check fail - invalid token");
            throw new CustomException("Invalid token", HttpStatus.UNAUTHORIZED);
        }
    }
}