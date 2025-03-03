package com.gnimtier.api.controller.auth;

import com.gnimtier.api.data.dto.basic.DataDto;
import com.gnimtier.api.service.auth.KakaoOauthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class KakaoOauthController {
    private final Logger LOGGER = LoggerFactory.getLogger(KakaoOauthController.class);
    private final KakaoOauthService kakaoOauthService;

    // 카카오 Oauth
    // json : data - tokens - Map<String, String>
    @Tag(name = "(Auth) 카카오 Oauth")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<?> kakaoOauth(@RequestParam("code") String code) {
        LOGGER.info("Kakao Oauth code: {}", code);
        String accessToken = kakaoOauthService.getAccessToken(code);
        return ResponseEntity.ok(new DataDto<>(Map.of("tokens", kakaoOauthService.getUserInfoAndReturnToken(accessToken))));
    }
}
