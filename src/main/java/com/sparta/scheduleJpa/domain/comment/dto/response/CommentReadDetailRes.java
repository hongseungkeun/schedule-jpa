package com.sparta.scheduleJpa.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CommentReadDetailRes(
        Long commentId,
        String userName,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {

    public static CommentReadDetailRes from(Comment comment) {
        return CommentReadDetailRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .userName(comment.getUser().getName())
                .build();
    }
}
