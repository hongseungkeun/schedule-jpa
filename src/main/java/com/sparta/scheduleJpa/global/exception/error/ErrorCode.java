package com.sparta.scheduleJpa.global.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다"),
    ALREADY_EXIST_USER(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    SCHEDULE_NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "일정을 찾을 수 없습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");

    private final HttpStatus status;
    private final String message;
}
