package com.sparta.scheduleJpa.domain.schedule.exception;

import com.sparta.scheduleJpa.global.exception.CustomRuntimeException;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;

public class ScheduleNotFoundException extends CustomRuntimeException {
    public ScheduleNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}