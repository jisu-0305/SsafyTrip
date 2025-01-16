package com.trip.ai.controller;

import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.service.AIService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {
    private final AIService aIService;

    @PostMapping("/weather")
    public ResponseEntity<WeatherAndClothesResponseDto> getWeatherAndClothes(@RequestBody ScheduleDetailDto scheduleDetail) {
        WeatherAndClothesResponseDto responseDto = aIService.getWeatherAndClothes(scheduleDetail);
        return ResponseEntity.ok(responseDto);
    }
}
