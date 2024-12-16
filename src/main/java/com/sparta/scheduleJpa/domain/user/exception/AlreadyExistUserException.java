package com.sparta.scheduleJpa.domain.user.exception;

import com.sparta.scheduleJpa.global.exception.CustomRuntimeException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class AlreadyExistUserException extends CustomRuntimeException {
    public AlreadyExistUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
