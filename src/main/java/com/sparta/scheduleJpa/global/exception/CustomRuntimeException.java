package com.sparta.scheduleJpa.global.exception;

import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
}
