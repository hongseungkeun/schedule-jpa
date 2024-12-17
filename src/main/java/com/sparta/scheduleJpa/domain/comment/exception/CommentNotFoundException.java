package com.sparta.scheduleJpa.domain.comment.exception;

import com.sparta.scheduleJpa.global.exception.CustomRuntimeException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class CommentNotFoundException extends CustomRuntimeException {
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
