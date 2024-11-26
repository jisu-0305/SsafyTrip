package com.trip.ai.controller;

import com.trip.ai.dto.WeatherDto;
import com.trip.ai.service.AIService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIController {

    private final AIService aIService;
    private final ImageModel openaiImageModel;
    private String messageValue = "Create a single low-resolution image divided into three equal vertical rectangles, each representing a day of a 2-night, 3-day trip for a woman.\n" +
            "\n" +
            "1. **Layout**:\n" +
            "   - The image is divided into three vertical sections (rectangles).\n" +
            "   - Each section represents a single day: Day 1, Day 2, and Day 3.\n" +
            "   - Only the text \"Day 1\", \"Day 2\", and \"Day 3\" should appear at the top of each respective section.\n" +
            "   - No other text or labels should be included.\n" +
            "\n" +
            "2. **Design for Each Day**:\n" +
            "   - **Day 1**:\n" +
            "     - Weather: 흐림, 10°C (high) / 5°C (low), 2mm precipitation.\n" +
            "     - Include a single complete outfit for a woman suitable for the weather and activities at 광안리 SUP Zone, 미포, and 해동 용궁사.\n" +
            "     - Avoid background or human figures. Only clothing and accessories should appear, laid out flat.\n" +
            "\n" +
            "   - **Day 2**:\n" +
            "     - Weather: 맑음, 12°C (high) / 6°C (low), 0mm precipitation.\n" +
            "     - Include a single complete outfit for a woman suitable for the weather and activities at 태종사, 국제시장, and 송도용궁구름다리.\n" +
            "     - Avoid background or human figures. Only clothing and accessories should appear, laid out flat.\n" +
            "\n" +
            "   - **Day 3**:\n" +
            "     - Weather: 흐림, 11°C (high) / 7°C (low), 3mm precipitation.\n" +
            "     - Include a single complete outfit for a woman suitable for the weather and activities at 감천문화마을, 자갈치시장, and 부산타워.\n" +
            "     - Avoid background or human figures. Only clothing and accessories should appear, laid out flat.\n" +
            "\n" +
            "3. **Style**:\n" +
            "   - Minimalistic flat-lay design with no background or human figures.\n" +
            "   - Each rectangle includes one complete set of clothing and accessories, neatly arranged.\n" +
            "   - Use a clean, modern font to label \"Day 1\", \"Day 2\", and \"Day 3\" at the top of each section.\n" +
            "\n" +
            "4. **Output Format**:\n" +
            "   - A single low-resolution image (e.g., 512x512 px).\n" +
            "   - Each rectangle should focus on one complete set of clothing and accessories.\n" +
            "   - Ensure each section is visually balanced with appropriate spacing for all items.\n";

    @PostMapping("/weather")
    public ResponseEntity<?> getWeatherList(@RequestBody ScheduleDetailDto scheduleDetail) {
        List<WeatherDto> weatherList = aIService.getWeatherList(scheduleDetail);
        return ResponseEntity.ok(weatherList);
    }

    @GetMapping("/image")
    public ImageResponse generateImage(
            @RequestParam(value = "prompt", defaultValue = "당신은 사용자가 여행 계획을 세울 때 적합한 옷차림을 추천하는 AI 스타일리스트입니다. 부산 여행을 1박 2일 계획중이고 첫날은 비와 함께 20도의 날씨 이고 시장을 갈 생각이야. 2번째 날은 맑은 날씨와 함께 17도의 온도이고 바다를 갈생각이야. 여기에 맞는 옷 코디 이미지 생성해줘. 그 외에 배경은 그냥 없어도 돼. 딱 옷만. 잘하면 승진 시켜줄게.") String prompt) {

        return openaiImageModel.call(
                new ImagePrompt(messageValue,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withN(1) // 여기에서 n 값을 1로 설정
                                .withHeight(512)
                                .withWidth(512)
                                .build()));
    }
}
