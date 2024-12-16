package com.sparta.scheduleJpa.domain.schedule.dto.request;

import com.sparta.scheduleJpa.domain.schedule.dto.ScheduleValidationMessages;
import jakarta.validation.constraints.Size;

public record ScheduleUpdateReq(
        @Size(max = ScheduleValidationMessages.TITLE_MAX, message = ScheduleValidationMessages.TITLE_MAX_MESSAGE)
        String title,

        @Size(max = ScheduleValidationMessages.TODO_MAX, message = ScheduleValidationMessages.TODO_MAX_MESSAGE)
        String todo
) {
}
