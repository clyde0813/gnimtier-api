package com.gnimtier.api.data.dto.gnt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
public class UserCommentDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserCommentResponseDto {
        private UserDto author;
        private String comment;
        private LocalDateTime createdAt;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserCommentRequestDto {
        private String comment;
    }

}
