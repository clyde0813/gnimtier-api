package com.gnimtier.api.data.entity.gnt;

import com.gnimtier.api.data.dto.gnt.UserCommentDto;
import com.gnimtier.api.data.dto.gnt.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_comment")
public class UserComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public UserCommentDto.UserCommentResponseDto toDto(UserDto authorDto) {
        UserCommentDto.UserCommentResponseDto userCommentResponseDto = new UserCommentDto.UserCommentResponseDto();
        userCommentResponseDto.setAuthor(authorDto);
        userCommentResponseDto.setComment(comment);
        userCommentResponseDto.setCreatedAt(createdAt);
        return userCommentResponseDto;
    }
}
