package com.trip.schedule.controller;

import com.trip.common.ResponseDto;
import com.trip.global.UnauthorizedException;
import com.trip.schedule.dto.ScheduleCreateRequestDto;
import com.trip.schedule.dto.ScheduleDetailDto;
import com.trip.schedule.dto.ScheduleInformationResponseDto;
import com.trip.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule Controller", description = "여행 일정 관리 API를 제공합니다.")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    @Operation(summary = "여행 일정 생성", description = "사용자가 새로운 여행 일정을 생성합니다.")
    public ResponseEntity<ResponseDto> createSchedule(
            @RequestBody ScheduleCreateRequestDto scheduleRequest,
            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        int scheduleId = scheduleService.createSchedule(userId, scheduleRequest);
        return ResponseEntity.status(201).body(ResponseDto.success(scheduleId + "로 등록"));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "여행 일정 삭제", description = "사용자가 여행 일정을 삭제합니다.")
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

    @GetMapping
    @Operation(summary = "사용자 여행 일정 목록 조회", description = "사용자가 등록한 모든 여행 일정의 제목과 메모를 반환합니다.")
    public ResponseEntity<List<ScheduleInformationResponseDto>> getAllSchedules(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        List<ScheduleInformationResponseDto> schedules = scheduleService.getAllSchedules(userId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/detail/{scheduleId}")
    @Operation(summary = "여행 일정 상세 조회", description = "특정 여행 일정의 상세 정보와 장소 목록을 날짜별로 반환합니다.")
    public ResponseEntity<ScheduleDetailDto> getScheduleDetail(
            @PathVariable("scheduleId") int scheduleId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        ScheduleDetailDto scheduleDetail = scheduleService.getScheduleDetail(userId, scheduleId);
        return ResponseEntity.ok(scheduleDetail);
    }
}
