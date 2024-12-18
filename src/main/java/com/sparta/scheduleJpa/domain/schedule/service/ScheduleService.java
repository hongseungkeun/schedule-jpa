package com.sparta.scheduleJpa.domain.schedule.service;

import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleUpdateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.entity.Schedule;
import com.sparta.scheduleJpa.domain.schedule.exception.ScheduleNotFoundException;
import com.sparta.scheduleJpa.domain.schedule.repository.ScheduleRepository;
import com.sparta.scheduleJpa.domain.user.entity.User;
import com.sparta.scheduleJpa.domain.user.service.UserService;
import com.sparta.scheduleJpa.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScheduleService {

    private final UserService userService;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public Long createSchedule(ScheduleCreateReq request, Long loginUserId) {
        User user = userService.findUserById(loginUserId);
        Schedule schedule = scheduleRepository.save(request.toEntity(user));

        return schedule.getId();
    }

    public Page<ScheduleReadDetailRes> readOverallSchedule(Pageable pageable) {
        return new PageImpl<>(
                scheduleRepository.findSchedulesAndCommentCountAll(pageable),
                pageable,
                scheduleRepository.count()
        );
    }

    public ScheduleReadDetailRes readDetailSchedule(Long scheduleId) {
        return scheduleRepository.findSchedulesAndCommentCountById(scheduleId);
    }

    @Transactional
    public void updateSchedule(Long scheduleId, ScheduleUpdateReq request, Long loginUserId) {
        Schedule schedule = checkUserAuthentication(scheduleId, loginUserId);

        schedule.update(request.title(), request.todo());
    }

    @Transactional
    public void deleteSchedule(Long scheduleId, Long loginUserId) {
        checkUserAuthentication(scheduleId, loginUserId);

        scheduleRepository.deleteById(scheduleId);
    }

    public Schedule findScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND_EXCEPTION));
    }

    private Schedule checkUserAuthentication(Long scheduleId, Long loginUserId) {
        Schedule schedule = findScheduleById(scheduleId);

        userService.checkUserAuthentication(schedule.getUser().getId(), loginUserId);

        return schedule;
    }
}
