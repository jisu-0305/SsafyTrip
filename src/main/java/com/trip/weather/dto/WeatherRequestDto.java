package com.trip.weather.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherRequestDto {
    private String date; // 예보 날짜 (yyyyMMdd 형식)
    private double latitude; // 위도
    private double longitude; // 경도
}