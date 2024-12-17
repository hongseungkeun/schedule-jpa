package com.sparta.scheduleJpa.domain.schedule.repository;

import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT new com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes" +
            "(s.id, s.user.name, s.title, s.todo, s.createdAt, s.updatedAt, COUNT(c.id)) FROM Schedule s LEFT JOIN Comment c " +
            "on s.id = c.schedule.id " +
            "GROUP BY s.id, s.user.name, s.title, s.todo, s.createdAt, s.updatedAt")
    List<ScheduleReadDetailRes> findAllSchedulesAndCommentCount(Pageable pageable);
}
