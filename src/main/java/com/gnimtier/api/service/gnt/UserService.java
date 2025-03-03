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
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
    }

    public SummonerResponseDto getRiotAccount(String userId) {
        String puuid = userPuuidRepository
                .findByUserId(userId)
                .orElseThrow(() -> new CustomException("Riot Account Not Found", HttpStatus.BAD_REQUEST))
                .getPuuid();
        return summonerService.getSummoner(puuid, false);
    }

    public void registerRiotAccount(User user, String puuid) {
        boolean userExists = userPuuidRepository.existsByUserId(user.getId());
        boolean puuidExists = userPuuidRepository.existsByPuuid(puuid);

        // 1인 1계정 운영방침
        if (userExists) {
            throw new CustomException("이미 등록된 계정이 있습니다.", HttpStatus.BAD_REQUEST);
        }
        // 중복 등록 방지
        if (puuidExists) {
            throw new CustomException("이미 등록된 계정입니다.", HttpStatus.BAD_REQUEST);
        }

        // summonerResponseDto from gnt-riot-api
        SummonerResponseDto summonerResponseDto = summonerService.getSummoner(puuid, false);
        // UserPuuid Object -> DB
        UserPuuid userPuuid = new UserPuuid(summonerResponseDto.getPuuid(), user.getId());
        userPuuidRepository.save(userPuuid);
    }
}
