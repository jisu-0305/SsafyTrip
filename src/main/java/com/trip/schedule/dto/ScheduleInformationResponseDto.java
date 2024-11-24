package com.trip.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInformationResponseDto {
    private int scheduleId;   // 여행 일정 ID
    private String title;     // 제목
    private String memo;      // 메모
    private LocalDate startDate; // 출발 날짜
    private LocalDate endDate;   // 도착 날짜
}
