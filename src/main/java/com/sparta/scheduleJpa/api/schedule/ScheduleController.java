package com.sparta.scheduleJpa.api.schedule;

import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleUpdateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.service.ScheduleService;
import com.sparta.scheduleJpa.global.util.SessionUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> createSchedule(
            @RequestBody @Valid final ScheduleCreateReq request,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        Long scheduleId = scheduleService.createSchedule(request, loginUserId);

        URI uri = UriComponentsBuilder.fromPath("/api/schedules/{scheduleId}")
                .buildAndExpand(scheduleId)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleReadDetailRes>> readSchedules() {
        return ResponseEntity.ok(scheduleService.readOverallSchedule());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleReadDetailRes> readSchedule(
            @PathVariable final Long scheduleId
    ) {
        return ResponseEntity.ok(scheduleService.readDetailSchedule(scheduleId));
    }

    @PatchMapping("/{scheduleId}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable final Long scheduleId,
            @RequestBody @Valid final ScheduleUpdateReq request,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        scheduleService.updateSchedule(scheduleId, request, loginUserId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable final Long scheduleId,
            @SessionAttribute(name = SessionUtil.SESSION_KEY) final Long loginUserId
    ) {
        scheduleService.deleteSchedule(scheduleId, loginUserId);

        return ResponseEntity.noContent().build();
    }
}
