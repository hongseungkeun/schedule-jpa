package com.sparta.scheduleJpa.domain.schedule.repository;

import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
