package com.sparta.scheduleJpa.domain.user.dto;

public class UserValidationMessages {
    public static final String EMAIL_PATTERN_REG = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]$";
    public static final String EMAIL_PATTERN_MESSAGE = "이메일 형식이 올바르지 않습니다.";
    public static final String EMAIL_BLANK_MESSAGE = "아이디를 입력해주세요.";
    public static final int PASSWORD_SIZE_MIN = 8;
    public static final String PASSWORD_SIZE_MIN_MESSAGE = "비밀번호를 8글자 이상 입력해주세요.";
    public static final String PASSWORD_BLANK_MESSAGE = "비밀번호를 입력해주세요.";
    public static final String NAME_BLANK_MESSAGE = "이름을 입력해주세요.";
    public static final int NAME_SIZE_MAX = 4;
    public static final String NAME_SIZE_MAX_MESSAGE = "이름을 4글자 이내로 입력해주세요.";
}