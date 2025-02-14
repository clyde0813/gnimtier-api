package com.gnimtier.api.service.auth;

import com.gnimtier.api.client.auth.KakaoOauthClient;
import com.gnimtier.api.config.security.JwtUtil;
import com.gnimtier.api.data.dto.auth.kakao.KakaoUserInfoResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.auth.UserOauth;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserOauthRepository;
import com.gnimtier.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoOauthService {
    private final KakaoOauthClient kakaoOauthClient;
    private final UserRepository userRepository;
    private final UserOauthRepository userOauthRepository;
    private final JwtUtil jwtUtil;

    private final Logger LOGGER = LoggerFactory.getLogger(KakaoOauthService.class);

    public String getAccessToken(String code) {
        return kakaoOauthClient.getAccessToken(code);
    }

    public Map<String, String> getUserInfoAndReturnToken(String accessToken) {
        LOGGER.info("[getUserInfoAndReturnToken] : {}", accessToken);
        KakaoUserInfoResponseDto userInfo = kakaoOauthClient.getUserInfo(accessToken);
        LOGGER.info("[getUserInfoAndReturnToken] - nickname : {}", userInfo.kakaoAccount.profile.nickName);
        String id4kakao = "kakao" + userInfo.id;
        Optional<UserOauth> userOauth = userOauthRepository.findById(id4kakao);
        LOGGER.info("[getUserInfoAndReturnToken] - userPresent : {}", userOauth.isPresent());
        // 신규 가입
        if (userOauth.isEmpty()) {
            LOGGER.info("[getUserInfoAndReturnToken] - create new user : {}", id4kakao);
            User newUser = new User();
            newUser.setNickname(userInfo.kakaoAccount.profile.nickName);
            newUser.setProfileImageUrl(userInfo.kakaoAccount.profile.profileImageUrl);
            newUser.setCreatedAt(LocalDateTime.now());
            // UserOauth에 넣을 id 떄문에 새로운 객체로 재할당
            User user = userRepository.save(newUser);
            LOGGER.info("[getUserInfoAndReturnToken] - new user id : {}", user.getId());
            UserOauth newUserOauth = new UserOauth();
            newUserOauth.setId(id4kakao);
            newUserOauth.setUserId(user.getId());
            newUserOauth.setProvider("kakao");
            userOauthRepository.save(newUserOauth);
            return Map.of("access_token", jwtUtil.generateAccessToken(user.getId()), "refresh_token", jwtUtil.generateRefreshToken(user.getId()));
        } else {
            // 기존 회원
            String userId = userOauth
                    .get()
                    .getUserId();
            return Map.of("access_token", jwtUtil.generateAccessToken(userId), "refresh_token", jwtUtil.generateRefreshToken(userId));
        }
    }
}
