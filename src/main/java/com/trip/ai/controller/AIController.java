package com.trip.ai.controller;

import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.service.AIService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.*;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aIService;
    private final StabilityAiImageModel stabilityAiImageModel;

    @PostMapping("/weather")
    public ResponseEntity<WeatherAndClothesResponseDto> getWeatherAndClothes(@RequestBody ScheduleDetailDto scheduleDetail) {
        WeatherAndClothesResponseDto responseDto = aIService.getWeatherAndClothes(scheduleDetail);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> generateImage(
            @RequestParam(value = "prompt", defaultValue = "You are an AI stylist helping a user plan a trip wardrobe. Create outfits suitable for a 2-day trip to Busan. Day 1: Rainy, 20°C, visiting a market. Day 2: Sunny, 17°C, visiting a beach. Focus on clothing only. Backgrounds are not required.") String prompt) {

        ImageResponse response = stabilityAiImageModel.call(
                new ImagePrompt(prompt,
                        StabilityAiImageOptions.builder()
                                .withSamples(1) // 생성 image 개수
                                .withHeight(512)
                                .withWidth(512)
                                .withCfgScale(9f)
                                .withSteps(20)
                                .withResponseFormat("image/png") // Response format
                                .build()));

        //이미지 데이터 가져오기
        Image image = response.getResult().getOutput();
        if (image == null) {
            return ResponseEntity.badRequest().body(null);
        }
        byte[] imageBytes;

        if (image.getB64Json() != null) {
            // Base64 데이터를 디코딩 -> 여기서 s3 또는 redis를 통한 이미지 관리가 필요할듯함
            imageBytes = Base64.getDecoder().decode(image.getB64Json());
        } else if (image.getUrl() != null) {
            // URL이 제공된 경우 에러 또는 별도 처리 (예: URL로부터 데이터를 다운로드)
            return ResponseEntity.badRequest()
                    .body(null); // URL 기반 처리는 필요에 따라 구현
        } else {
            return ResponseEntity.badRequest().body(null); // 유효한 이미지 데이터가 없음
        }

        // 반환된 이미지를 클라이언트로 전달
        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(imageBytes);
    }
}
