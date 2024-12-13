package com.sparta.scheduleJpa.domain.schedule.dto.request;

import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.user.entity.User;

public record ScheduleCreateReq(
        Long id,
        String title,
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
