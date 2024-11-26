package com.trip.ai.service;

import com.trip.ai.dto.WeatherDto;
import com.trip.schedule.dto.ScheduleDetailDto;

import java.util.List;

public interface AIService {
    List<WeatherDto> getWeatherList(ScheduleDetailDto scheduleDetail);
}
