package com.trip.weather.controller;

import com.trip.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<?> getWeatherForecast(
            @RequestParam double latitude,
            @RequestParam double longitude) {
        try {
            String weatherData = weatherService.getWeatherForecast(latitude, longitude);
            return ResponseEntity.ok(weatherData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
