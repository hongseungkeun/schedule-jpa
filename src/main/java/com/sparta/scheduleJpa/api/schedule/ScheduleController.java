package com.sparta.scheduleJpa.api.schedule;

import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleCreateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.request.ScheduleUpdateReq;
import com.sparta.scheduleJpa.domain.schedule.dto.response.ScheduleReadDetailRes;
import com.sparta.scheduleJpa.domain.schedule.service.ScheduleService;
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
            @RequestBody final ScheduleCreateReq request
    ) {
        Long scheduleId = scheduleService.createSchedule(request);

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
            @RequestBody final ScheduleUpdateReq request
    ) {
        scheduleService.updateSchedule(scheduleId, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable final Long scheduleId
    ) {
        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.noContent().build();
    }
}
