package com.trip.schedule.service;

import com.trip.schedule.dto.ScheduleCreateRequestDto;
import com.trip.schedule.dto.ScheduleInformationDto;
import com.trip.schedule.dto.SchedulePlaceDto;
import com.trip.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;

    @Transactional
    @Override
    public int createSchedule(Long userId, ScheduleCreateRequestDto scheduleCreateRequestDto) {
        ScheduleInformationDto scheduleInfo = scheduleCreateRequestDto.getScheduleInformation();
        scheduleMapper.insertSchedule(userId, scheduleInfo);

        int scheduleId = scheduleInfo.getScheduleId();
        for (SchedulePlaceDto place : scheduleCreateRequestDto.getSchedulePlaces()) {
            scheduleMapper.insertSchedulePlace(scheduleId, place);
        }

        return scheduleId;
    }

    @Transactional
    @Override
    public void deleteSchedule(Long userId, int scheduleId) {
        //DB CASCADE 설정을 통해 Schedule 삭제 -> 해당 ScheduleId를 가진 places 제거
        scheduleMapper.deleteSchedule(userId, scheduleId);
    }
}

