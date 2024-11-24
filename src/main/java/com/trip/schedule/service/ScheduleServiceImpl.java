package com.trip.schedule.service;

import com.trip.schedule.dto.*;
import com.trip.schedule.mapper.ScheduleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<ScheduleInformationListResponseDto> getAllSchedules(Long userId) {
        return scheduleMapper.selectAllSchedules(userId);
    }

    @Override
    public ScheduleDetailResponseDto getScheduleDetail(Long userId, int scheduleId) {
        ScheduleInformationDto schedule = scheduleMapper.selectScheduleById(userId, scheduleId);

        List<SchedulePlaceDto> schedulePlaces = scheduleMapper.selectSchedulePlaces(scheduleId);

        Map<String, List<SchedulePlaceDto>> groupedByDate = schedulePlaces.stream()
                .collect(Collectors.groupingBy(place -> place.getVisitTime().toLocalDate().toString()));

        List<SchedulePlacesByDateDto> schedulePlacesByDate = groupedByDate.entrySet().stream()
                .map(entry -> new SchedulePlacesByDateDto(entry.getKey(), entry.getValue())) // Map Entry -> DTO 변환
                .sorted(Comparator.comparing(SchedulePlacesByDateDto::getDate)) // 날짜 기준 정렬
                .collect(Collectors.toList());

        return new ScheduleDetailResponseDto(schedule, schedulePlacesByDate);
    }
}
