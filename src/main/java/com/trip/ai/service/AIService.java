package com.trip.ai.service;

import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.schedule.dto.ScheduleDetailDto;

public interface AIService {
    WeatherAndClothesResponseDto getWeatherAndClothes(ScheduleDetailDto scheduleDetail);
}
