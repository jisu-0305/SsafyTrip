package com.trip.weather.controller;

import com.trip.weather.dto.WeatherRequestDto;
import com.trip.weather.dto.WeatherResponseDto;
import com.trip.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/forecast")
    public List<WeatherResponseDto> getWeatherForecast(
            @RequestBody List<WeatherRequestDto> weatherRequests) {
        // Service로 요청을 전달하여 처리 후 결과 반환
        return weatherService.getWeatherForecast(weatherRequests);
    }
}