package com.gnimtier.api.data.entity.auth;

import com.gnimtier.api.data.dto.gnt.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = false, nullable = true)
    private String nickname;

    @Column(nullable = true)
    private String profileImageUrl;

    @Column(nullable = false)
    private String password;

    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setNickname(nickname);
        userDto.setProfileImageUrl(profileImageUrl);
        return userDto;
    }
}
