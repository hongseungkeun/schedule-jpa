package com.sparta.scheduleJpa.domain.schedule.dto.request;

import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;

public record ScheduleCreateReq(
        String userName,
        String title,
        String todo
) {

    public Schedule toEntity() {
        return Schedule.builder()
                .userName(this.userName)
                .title(this.title)
                .todo(this.todo)
                .build();
    }
}
