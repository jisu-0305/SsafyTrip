package com.trip.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleInformationListResponseDto {
    private int scheduleId; // 여행 일정 ID
    private String title;   // 여행 제목
    private String memo;    // 여행 메모
}
