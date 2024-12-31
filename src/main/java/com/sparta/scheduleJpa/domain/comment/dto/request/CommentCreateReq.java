package com.sparta.scheduleJpa.domain.comment.dto.request;

import com.sparta.scheduleJpa.domain.comment.dto.CommentValidationMessages;
import com.sparta.scheduleJpa.domain.comment.entity.Comment;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateReq(
        @Size(max = CommentValidationMessages.CONTENT_MAX, message = CommentValidationMessages.CONTENT_MAX_MESSAGE)
        @NotBlank(message = CommentValidationMessages.CONTENT_BLANK_MESSAGE)
        String content,
        Long scheduleId
) {

    public Comment toEntity(Schedule schedule, User user) {
        return Comment.builder()
                .content(this.content)
                .schedule(schedule)
                .user(user)
                .build();
    }
}
