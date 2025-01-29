package com.gnimtier.api.controller.auth;

import com.gnimtier.api.service.auth.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/oauth/kakao")
    public Map<String, String> kakaoOauth(
            @RequestParam("code") String code
    ) {
        LOGGER.info("Kakao Oauth code: {}", code);
        String accessToken = kakaoOauthService.getAccessToken(code);
        return kakaoOauthService.getUserInfoAndReturnToken(accessToken);
    }
}
