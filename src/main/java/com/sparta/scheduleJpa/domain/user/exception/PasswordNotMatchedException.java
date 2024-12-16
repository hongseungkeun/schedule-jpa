package com.sparta.scheduleJpa.domain.user.exception;

import com.sparta.scheduleJpa.global.exception.CustomRuntimeException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class PasswordNotMatchedException extends CustomRuntimeException {
    public PasswordNotMatchedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
