package com.gnimtier.api.service.gnt;

import com.gnimtier.api.data.dto.gnt.UserCommentDto;
import com.gnimtier.api.data.dto.riot.client.PageableDto;
import com.gnimtier.api.data.entity.auth.User;
import com.gnimtier.api.data.entity.gnt.UserComment;
import com.gnimtier.api.exception.CustomException;
import com.gnimtier.api.repository.UserCommentRepository;
import com.gnimtier.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommentService {
    private final UserRepository userRepository;
    private final UserCommentRepository userCommentRepository;

    private final int PAGE_SIZE = 5;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    // 사용자 댓글 조회
    public PageableDto.PageableResponseDto<UserCommentDto.UserCommentResponseDto> getUserComment(String userId, PageableDto.PlainRequestDto plainRequestDto) {
        LOGGER.info("[UserCommentService.getUserComment] - userId : {}", userId);

        if (!userRepository.existsById(userId)) {
            LOGGER.error("[UserCommentService.getUserComment] - User not found, userId : {}", userId);
            throw new CustomException("USER NOT FOUND", HttpStatus.NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(plainRequestDto.getPage(), PAGE_SIZE);
        Page<UserComment> userCommentPage = userCommentRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

        PageableDto.PageableResponseDto<UserCommentDto.UserCommentResponseDto> userCommentResponseDto = new PageableDto.PageableResponseDto<>();
        List<UserCommentDto.UserCommentResponseDto> userCommentList = new ArrayList<>();


        userCommentPage.forEach(UserComment -> {
            Optional<User> author = userRepository.findById(UserComment.getAuthorId());
            userCommentList.add(UserComment.toDto(author
                    .get()
                    .getUserDto()));
        });

        userCommentResponseDto.setData(userCommentList);
        userCommentResponseDto.setSortBy("date");
        userCommentResponseDto.setPage(userCommentPage.getNumber());
        userCommentResponseDto.setPageSize(userCommentPage.getNumberOfElements());
        userCommentResponseDto.setHasNext(userCommentPage.hasNext());
        userCommentResponseDto.setHasPrevious(userCommentPage.hasPrevious());

        return userCommentResponseDto;

    }

    // 사용자 댓글 등록
    public void postUserComment(User user, String userId, UserCommentDto.UserCommentRequestDto userCommentRequestDto) {
        if (!userRepository.existsById(userId)) {
            LOGGER.error("[UserCommentService.getUserComment] - User not found, userId : {}", userId);
            throw new CustomException("USER NOT FOUND", HttpStatus.NOT_FOUND);
        }

        String authorId = user.getId();
        LOGGER.info("[UserCommentService.postUserComment] - userId : {}, authorId : {}", userId, authorId);
        UserComment userComment = new UserComment();
        userComment.setUserId(userId);
        userComment.setAuthorId(authorId);
        userComment.setComment(userCommentRequestDto.getComment());
        userComment.setCreatedAt(LocalDateTime.now());
        userCommentRepository.save(userComment);
    }
}
