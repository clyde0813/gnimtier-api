package com.gnimtier.api.data.entity.auth;

import com.gnimtier.api.data.dto.gnt.UserDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

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

    @Column(name = "nickname", unique = false, nullable = true)
    private String nickname;

    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

//    @Column(nullable = false)
//    private String password;

    // toDto 로 리펙해야됨
    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setNickname(nickname);
        userDto.setProfileImageUrl(profileImageUrl);
        return userDto;
    }
}
