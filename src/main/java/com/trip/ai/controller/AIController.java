package com.trip.ai.controller;

import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.service.AIService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aIService;
    private final ImageModel openaiImageModel;

    @PostMapping("/weather")
    public ResponseEntity<WeatherAndClothesResponseDto> getWeatherAndClothes(@RequestBody ScheduleDetailDto scheduleDetail) {
        WeatherAndClothesResponseDto responseDto = aIService.getWeatherAndClothes(scheduleDetail);
        return ResponseEntity.ok(responseDto);
    }

//    @GetMapping("/image")
//    public ImageResponse generateImage(
//            @RequestParam(value = "prompt", defaultValue = "당신은 사용자가 여행 계획을 세울 때 적합한 옷차림을 추천하는 AI 스타일리스트입니다. 부산 여행을 1박 2일 계획중이고 첫날은 비와 함께 20도의 날씨 이고 시장을 갈 생각이야. 2번째 날은 맑은 날씨와 함께 17도의 온도이고 바다를 갈생각이야. 여기에 맞는 옷 코디 이미지 생성해줘. 그 외에 배경은 그냥 없어도 돼. 딱 옷만. 잘하면 승진 시켜줄게.") String prompt) {
//
//        return openaiImageModel.call(
//                new ImagePrompt(prompt,
//                        OpenAiImageOptions.builder()
//                                .withQuality("standard")
//                                .withN(1) // 여기에서 n 값을 1로 설정
//                                .withHeight(256)
//                                .withWidth(256)
//                                .build()));
//    }
}
