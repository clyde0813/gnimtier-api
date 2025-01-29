package com.gnimtier.api.data.entity.riot;

import com.gnimtier.api.data.entity.auth.User;
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
@Table(name = "user_puuid")
public class UserPuuid {
    @Id
    private String puuid;

    @Column(name = "user_id")
    private String userId;
}
