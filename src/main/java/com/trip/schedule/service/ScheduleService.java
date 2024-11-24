package com.trip.schedule.service;

import com.trip.schedule.dto.ScheduleCreateRequestDto;

public interface ScheduleService {
    int createSchedule(Long userId, ScheduleCreateRequestDto scheduleRequest);

    void deleteSchedule(Long userId, int scheduleId);
}
