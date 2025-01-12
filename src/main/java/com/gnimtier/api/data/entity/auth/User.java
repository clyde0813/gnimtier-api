package com.gnimtier.api.data.entity.auth;

import com.gnimtier.api.data.dto.gnt.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;

    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        return userDto;
    }
}
