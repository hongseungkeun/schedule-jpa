package com.sparta.scheduleJpa.domain.user.exception;

import com.sparta.scheduleJpa.global.exception.CustomRuntimeException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class UserNotFoundException extends CustomRuntimeException {
    public UserNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
