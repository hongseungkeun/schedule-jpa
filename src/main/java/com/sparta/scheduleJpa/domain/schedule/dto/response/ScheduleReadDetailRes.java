package com.sparta.scheduleJpa.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record ScheduleReadDetailRes(
        Long scheduleId,
        String userName,
        String title,
        String todo,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt,
        Long commentCount
) {

    public static ScheduleReadDetailRes from(Schedule schedule) {
        return ScheduleReadDetailRes.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .todo(schedule.getTodo())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .userName(schedule.getUser().getName())
                .build();
    }
}
