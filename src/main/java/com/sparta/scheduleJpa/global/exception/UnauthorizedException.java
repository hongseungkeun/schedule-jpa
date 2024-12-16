package com.sparta.scheduleJpa.global.exception;

import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class UnauthorizedException extends CustomRuntimeException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}