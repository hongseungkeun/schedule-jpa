package com.sparta.scheduleJpa.domain.schedule.dto;

public class ScheduleValidationMessages {
    public static final int TITLE_MAX = 10;
    public static final String TITLE_MAX_MESSAGE = "제목은 10자 이내로 적을 수 있습니다.";
    public static final String TITLE_BLANK_MESSAGE = "제목을 입력해주세요.";
    public static final int TODO_MAX = 200;
    public static final String TODO_MAX_MESSAGE = "글은 200자 이내로 적을 수 있습니다.";
    public static final String TODO_BLANK_MESSAGE = "글을 입력해주세요.";
}
