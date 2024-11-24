package com.trip.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDetailResponseDto {
    private ScheduleInformationDto schedule;           // 여행 일정 기본 정보
    private List<SchedulePlacesByDateDto> schedulePlacesByDate; // 날짜별 장소 리스트
}
