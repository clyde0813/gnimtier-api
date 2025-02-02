package com.gnimtier.api.data.entity.gnt;

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
@Table(name = "user_group_association")
public class UserGroupAssociation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    // 성능 튜닝용
    @Column(name = "puuid")
    private String puuid;

    @Column(name = "group_id")
    private String groupId;
}
