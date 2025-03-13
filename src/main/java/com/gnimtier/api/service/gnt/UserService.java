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
        LOGGER.info("[getUserByUserId] - Getting User");
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException("USER NOT FOUND", HttpStatus.NOT_FOUND));
    }

    public SummonerResponseDto getRiotAccount(String userId) {
        LOGGER.info("[getRiotAccount] - Getting Riot Account");
        String puuid = userPuuidRepository
                .findByUserId(userId)
                .orElseThrow(() -> new CustomException("RIOT ACCOUNT NOT FOUND", HttpStatus.BAD_REQUEST))
                .getPuuid();
        return summonerService.getSummoner(puuid, false);
    }


    public void registerRiotAccount(User user, String puuid) {
        LOGGER.info("[registerRiotAccount] - Registering Riot Account");
        boolean userExists = userPuuidRepository.existsByUserId(user.getId());
        boolean puuidExists = userPuuidRepository.existsByPuuid(puuid);

        // 1인 1계정 운영방침
        if (userExists) {
            LOGGER.error("[registerRiotAccount] - User already has a Riot Account");
            throw new CustomException("ALREADY REGISTERED USER ACCOUNT.", HttpStatus.BAD_REQUEST);
        }
        // 중복 등록 방지
        if (puuidExists) {
            LOGGER.error("[registerRiotAccount] - Riot Account already registered");
            throw new CustomException("ALREADY REGISTERED RIOT ACCOUNT.", HttpStatus.BAD_REQUEST);
        }

        // summonerResponseDto from gnt-riot-api
        SummonerResponseDto summonerResponseDto = summonerService.getSummoner(puuid, false);
        // UserPuuid Object -> DB
        UserPuuid userPuuid = new UserPuuid(summonerResponseDto.getPuuid(), user.getId());
        userPuuidRepository.save(userPuuid);
    }
}
