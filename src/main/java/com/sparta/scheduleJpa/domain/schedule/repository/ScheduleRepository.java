package com.sparta.scheduleJpa.domain.schedule.repository;

import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes" +
            "(s.id, s.title, s.todo, s.createdAt, s.updatedAt, s.user.id, s.user.name, COUNT(c.id)) FROM Schedule s LEFT JOIN Comment c " +
            "ON s.id = c.schedule.id " +
            "GROUP BY s.id, s.title, s.todo, s.createdAt, s.updatedAt, s.user.id, s.user.name")
    List<ScheduleReadDetailRes> findSchedulesAndCommentCountAll(Pageable pageable);

    @Query("SELECT new com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes" +
            "(s.id, s.title, s.todo, s.createdAt, s.updatedAt, s.user.id, s.user.name, COUNT(c.id)) FROM Schedule s LEFT JOIN Comment c " +
            "ON s.id = c.schedule.id " +
            "WHERE s.id = :schedule_id " +
            "GROUP BY s.id, s.title, s.todo, s.createdAt, s.updatedAt, s.user.id, s.user.name")
    ScheduleReadDetailRes findSchedulesAndCommentCountById(@Param("schedule_id") Long scheduleId);
}
