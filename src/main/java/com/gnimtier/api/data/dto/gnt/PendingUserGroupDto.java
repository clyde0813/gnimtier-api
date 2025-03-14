package com.gnimtier.api.data.dto.gnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PendingUserGroupDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PendingUserGroupResponseDto {
        private String id;
        private String name;
        private String description;
        private int voteCount;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PendingUserGroupRequestDto {
        private String name;
        private String description;
    }
}
