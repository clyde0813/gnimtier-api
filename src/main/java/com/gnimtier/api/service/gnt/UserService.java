package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.riot.internal.response.SummonerResponseDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.riot.UserPuuid;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserPuuidRepository;
import com.gnimtier.api.repository.UserRepository;
import com.gnimtier.api.service.riot.tft.SummonerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserPuuidRepository userPuuidRepository;
    private final SummonerService summonerService;

    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public User getUserByUserId(String userId) {
        return userRepository
                .findById(userId)
                .orElse(null);
    }

    public SummonerResponseDto getRiotAccount(User user) {
        String puuid = userPuuidRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new CustomException("등록된 계정이 없습니다.", HttpStatus.BAD_REQUEST))
                .getPuuid();
        return summonerService.getSummoner(puuid, false);
    }

    public void registerRiotAccount(User user, String puuid) {
        // 1인 1계정 등록 로직
        if (userPuuidRepository
                .findByUserId(user.getId())
                .isPresent()) {
            LOGGER.error("[UserService.registerRiotAccount()] User already has an account registered");
            throw new CustomException("이미 등록된 계정이 있습니다.", HttpStatus.BAD_REQUEST);
        }
        // 중복 등록 방지 로직
        if (userPuuidRepository
                .findByPuuid(puuid)
                .isPresent()) {
            LOGGER.error("[UserService.registerRiotAccount()] Puuid already registered");
            throw new CustomException("이미 등록된 계정입니다.", HttpStatus.BAD_REQUEST);
        }
        // 등록되지 않았을 경우 등록 진행
        SummonerResponseDto summonerResponseDto = summonerService.getSummoner(puuid, false);
        UserPuuid userPuuid = new UserPuuid();
        userPuuid.setPuuid(summonerResponseDto.getPuuid());
        userPuuid.setUserId(user.getId());
        userPuuidRepository.save(userPuuid);
        LOGGER.info("[UserService.registerRiotAccount()] Riot account registered");
    }
}
