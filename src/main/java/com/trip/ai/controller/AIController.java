package com.trip.ai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.service.AIService;
import com.trip.review.dto.S3ResponseDTO;
import com.trip.review.util.S3Util;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.*;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aIService;

    @PostMapping("/weather")
    public ResponseEntity<WeatherAndClothesResponseDto> getWeatherAndClothes(@RequestBody ScheduleDetailDto scheduleDetail) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(scheduleDetail);
            System.out.println("Received JSON: " + json);
        } catch (Exception e) {
            System.err.println("Failed to convert to JSON: " + e.getMessage());
        }

        WeatherAndClothesResponseDto responseDto = aIService.getWeatherAndClothes(scheduleDetail);
        return ResponseEntity.ok(responseDto);
    }
}
