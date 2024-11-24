package com.trip.schedule.service;

import com.trip.schedule.dto.ScheduleCreateRequestDto;
import com.trip.schedule.dto.ScheduleDetailResponseDto;
import com.trip.schedule.dto.ScheduleInformationResponseDto;

import java.util.List;

public interface ScheduleService {
    int createSchedule(Long userId, ScheduleCreateRequestDto scheduleRequest);

    void deleteSchedule(Long userId, int scheduleId);

    List<ScheduleInformationResponseDto> getAllSchedules(Long userId);

    ScheduleDetailResponseDto getScheduleDetail(Long userId, int scheduleId);
}
