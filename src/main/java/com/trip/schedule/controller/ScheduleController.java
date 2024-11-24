package com.trip.schedule.controller;

import com.trip.common.ResponseDto;
import com.trip.schedule.dto.ScheduleCreateRequestDto;
import com.trip.schedule.service.ScheduleService;
import com.trip.global.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ResponseDto> createSchedule(
            @RequestBody ScheduleCreateRequestDto scheduleRequest,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        int scheduleId = scheduleService.createSchedule(userId, scheduleRequest);
        return ResponseEntity.status(201).body(ResponseDto.success(scheduleId+"로 등록"));
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<ResponseDto> deleteSchedule(
            @PathVariable("scheduleId") int scheduleId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ResponseDto.failure("로그인이 필요합니다."));
        }

        scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.ok(ResponseDto.success("Schedule deleted successfully."));
    }
}
