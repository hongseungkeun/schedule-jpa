package com.sparta.scheduleJpa.domain.schedule.dto.request;

import com.sparta.scheduleJpa.domain.schedule.dto.ScheduleValidationMessages;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScheduleCreateReq(
        @Size(max = ScheduleValidationMessages.TITLE_MAX, message = ScheduleValidationMessages.TITLE_MAX_MESSAGE)
        @NotBlank(message = ScheduleValidationMessages.TITLE_BLANK_MESSAGE)
        String title,

        @Size(max = ScheduleValidationMessages.TODO_MAX, message = ScheduleValidationMessages.TODO_MAX_MESSAGE)
        @NotBlank(message = ScheduleValidationMessages.TODO_BLANK_MESSAGE)
        String todo
) {

    public Schedule toEntity(User user) {
        return Schedule.builder()
                .title(this.title)
                .todo(this.todo)
                .user(user)
                .build();
    }
}
