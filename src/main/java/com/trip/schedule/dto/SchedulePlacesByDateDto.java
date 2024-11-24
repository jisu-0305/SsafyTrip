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
public class SchedulePlacesByDateDto {
    private String date;                // 날짜 (yyyy-MM-dd 형식)
    private List<SchedulePlaceDto> places; // 해당 날짜의 장소 리스트
}