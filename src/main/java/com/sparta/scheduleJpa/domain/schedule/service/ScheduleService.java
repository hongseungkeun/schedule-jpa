package com.sparta.scheduleJpa.domain.schedule.service;

import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleUpdateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.schedule.exception.ScheduleNotFoundException;
import com.sparta.scheduleJpa.domain.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long createSchedule(ScheduleCreateReq request) {
        Schedule schedule = scheduleRepository.save(request.toEntity());
        return schedule.getId();
    }

    public List<ScheduleReadDetailRes> readOverallSchedule() {
        return scheduleRepository.findAll()
                .stream()
                .map(ScheduleReadDetailRes::from)
                .toList();
    }

    public ScheduleReadDetailRes readDetailSchedule(Long scheduleId) {
        return ScheduleReadDetailRes.from(findScheduleById(scheduleId));
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateReq request) {
        Schedule schedule = findScheduleById(scheduleId);

        schedule.updateSchedule(request.title(), request.todo());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    private Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("일정을 찾을 수 없습니다"));
    }
}
