package com.sparta.scheduleJpa.domain.comment.dto.request;

import com.sparta.scheduleJpa.domain.comment.dto.CommentValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentUpdateReq(
        @Size(max = CommentValidationMessages.CONTENT_MAX, message = CommentValidationMessages.CONTENT_MAX_MESSAGE)
        @NotBlank(message = CommentValidationMessages.CONTENT_BLANK_MESSAGE)
        String content
) {
}
