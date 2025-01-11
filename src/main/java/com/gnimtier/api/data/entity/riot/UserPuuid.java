package com.gnimtier.api.data.entity.riot;

import com.gnimtier.api.data.entity.auth.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPuuid {
    @Id
    private String puuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
