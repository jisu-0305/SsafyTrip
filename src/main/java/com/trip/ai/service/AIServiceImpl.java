package com.trip.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.dto.WeatherDto;
import com.trip.attraction.service.AttractionService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AttractionService attractionService;
    private final ChatClient chatClient;
    private final ImageModel imageModel;
    private final ObjectMapper objectMapper;

    @Override
    public WeatherAndClothesResponseDto getWeatherAndClothes(ScheduleDetailDto scheduleDetail) {
        // 1. WeatherDto 리스트 생성
        List<WeatherDto> weatherList = getWeatherList(scheduleDetail);

        // 2. 코디 프롬프트 생성
        String clothesPrompt = generateClothesPrompt(weatherList);
        System.out.println("prompt: "+clothesPrompt);

        // 3. 코디 이미지 생성 및 URL 추출
        String clothesURL = generateImageURL(clothesPrompt);
//        String clothesURL = "dummyURL";

        // 4. 준비물 DTO 생성 및 추출
        String supplies = generateSupplies(weatherList);

        return new WeatherAndClothesResponseDto(weatherList, clothesURL, supplies);
    }

    private List<WeatherDto> getWeatherList(ScheduleDetailDto scheduleDetail) {
        // Weather 프롬프트 생성
        String prompt = generateWeatherPrompt(scheduleDetail);

        // AI로부터 JSON 응답 받기
        String jsonResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // 순수 JSON 데이터 추출
        String cleanedJsonResponse = extractPureJson(jsonResponse);

        // WeatherDto 리스트로 변환
        try {
            return objectMapper.readValue(cleanedJsonResponse, new TypeReference<List<WeatherDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data from AI response", e);
        }
    }

    private String generateWeatherPrompt(ScheduleDetailDto scheduleDetail) {
        Map<String, List<String>> titlesByDate = attractionService.getTitlesByDate(scheduleDetail);
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("Please extract the locations from the provided schedule for each date, ")
                .append("combine the locations for each date into a single string separated by commas, and ")
                .append("return the weather information as a JSON array that matches the following Java DTO class:\n\n");

        // WeatherDto 구조
        promptBuilder.append("public class WeatherDto {\n")
                .append("    private String date; // Date in the format 'YYYY-MM-DD'\n")
                .append("    private String location; // Combined locations for the date\n")
                .append("    private double highTemperature; // High temperature in Celsius\n")
                .append("    private double lowTemperature; // Low temperature in Celsius\n")
                .append("    private String weatherCondition; // e.g., '맑음', '흐림'\n")
                .append("    private String precipitation; // e.g., '10mm'\n")
                .append("}\n\n");

        // 일정 데이터 추가
        promptBuilder.append("**Start date: ").append(scheduleDetail.getSchedule().getStartDate()).append("**\n\n");
        titlesByDate.forEach((date, titles) -> {
            promptBuilder.append(date).append(" day:\n")
                    .append("- Locations: ").append(String.join(", ", titles)).append("\n");
        });

        // 출력 형식 안내
        promptBuilder.append("\nReturn the data as a JSON array matching the WeatherDto class above. ")
                .append("Do not include any additional text or explanation, only the JSON array.");

        return promptBuilder.toString();
    }

    private String generateClothesPrompt(List<WeatherDto> weatherList) {
        StringBuilder promptBuilder = new StringBuilder();

        promptBuilder.append("여행일정과 날씨를 참고하여 옷만 존재하는 코디북을 얻고 싶습니다. 한 이미지 안에 날짜 사이즈만큼의 직사각형으로 분리하여, 각 날짜별 코디를 넣어주세요. 아래 JSON 데이터를 참고하여 이미지를 제작하세요. 이미지는 코디만 포함하며, 텍스트나 불필요한 배경 이미지는 넣지 마세요.");

        int day = 1;
        for (WeatherDto weather : weatherList) {
            promptBuilder.append("Day ").append(day).append(":\n")
                    .append("- Date: ").append(weather.getDate()).append("\n")
                    .append("- Location: ").append(weather.getLocation()).append("\n")
                    .append("- Weather: ").append(weather.getWeatherCondition()).append(", ")
                    .append(weather.getHighTemperature()).append("°C / ")
                    .append(weather.getLowTemperature()).append("°C, ")
                    .append(weather.getPrecipitation()).append("\n\n");
            day++;
        }

        promptBuilder.append("코디북 이미지는 다음 JSON 데이터를 기반으로 만들어주세요. ");
        return promptBuilder.toString();
    }

    private String generateSupplies(List<WeatherDto> weatherList) {
        StringBuilder prompt = new StringBuilder("다음 날씨와 장소를 참고하여 필요한 여행 준비물을 추천해주세요. 카테고리별로 나눠주세요. 200바이트 이내로 작성해주세요.\n\n");

        int day = 1;
        for (WeatherDto weather : weatherList) {
            prompt.append("Day ").append(day).append(":\n")
                    .append("- 날짜: ").append(weather.getDate()).append("\n")
                    .append("- 장소: ").append(weather.getLocation()).append("\n")
                    .append("- 온도: ").append(weather.getHighTemperature()).append("°C / ")
                    .append(weather.getLowTemperature()).append("°C\n\n");
            day++;
        }

        prompt.append("카테고리1: 의류 (양말, 속옷 등)\n")
                .append("카테고리2: 스킨케어/면도용품 (스킨, 로션, 선크림 등)\n")
                .append("카테고리3: 헤어용품 (고데기, 에센스 등)\n")
                .append("etc: 기본 필수품 (주민등록증, 여권 등)")
                 .append("특수문자 (예: ##, **, !! 등)또는 마크다운 문법 사용하지 말고, 간단하고 읽기 쉽게 작성해주세요. 추가로 줄바꿈은 카테고리 변경시에만 쓰세요");

        // AI로부터 응답 받기
        String response = chatClient.prompt()
                .user(prompt.toString())
                .call()
                .content();

        return response.trim();
    }


    private String generateImageURL(String prompt) {
        try {
            var response = imageModel.call(
                    new ImagePrompt(prompt, OpenAiImageOptions.builder()
                            .withQuality("standard")
                            .withHeight(1024)
                            .withWidth(1024)
                            .withN(1)
                            .build())
            );

            var output = response.getResult().getOutput();
            if (output != null && output.getUrl() != null) {
                return output.getUrl();
            }

            if (response.getResults() != null && !response.getResults().isEmpty()) {
                return response.getResults().get(0).getOutput().getUrl();
            }

            throw new RuntimeException("Image URL not found in AI response.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image URL", e);
        }
    }

    private String extractPureJson(String response) {
        int startIndex = response.indexOf("[");
        int endIndex = response.lastIndexOf("]");
        if (startIndex != -1 && endIndex != -1) {
            return response.substring(startIndex, endIndex + 1).trim();
        } else {
            throw new RuntimeException("Invalid JSON format in AI response");
        }
    }
}
