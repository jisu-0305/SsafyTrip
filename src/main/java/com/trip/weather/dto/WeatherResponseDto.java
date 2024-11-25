package com.trip.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponseDto {
    private String date; // 날짜
    private String weather; // 날씨 상태 (맑음, 흐림 등)
    private String temperature; // 기온
}