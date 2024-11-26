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

        // 3. 코디 이미지 생성 및 URL 추출
        String clothesURL = generateImageURL(clothesPrompt);

        // 4. WeatherAndClothesResponseDto 반환
        return new WeatherAndClothesResponseDto(weatherList, clothesURL);
    }

    private List<WeatherDto> getWeatherList(ScheduleDetailDto scheduleDetail) {
        // Weather 프롬프트 생성
        String prompt = generateWeatherPrompt(scheduleDetail);

        // AI로부터 JSON 응답 받기
        String jsonResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        System.out.println("weahter:"+jsonResponse.toString());
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

        promptBuilder.append("Create a single low-resolution image divided into three equal vertical rectangles. ")
                .append("Each rectangle represents one day, with the text 'Day 1', 'Day 2', and 'Day 3' at the top-center. ")
                .append("Include appropriate clothing and accessories for a woman based on the following weather conditions:\n\n");

        int day = 1;
        for (WeatherDto weather : weatherList) {
            promptBuilder.append("Day ").append(day).append(":\n")
                    .append("- Date: ").append(weather.getDate()).append("\n")
                    .append("- Location: ").append(weather.getLocation()).append("\n")
                    .append("- Weather: ").append(weather.getWeatherCondition()).append(", ")
                    .append(weather.getHighTemperature()).append("°C (high) / ")
                    .append(weather.getLowTemperature()).append("°C (low), ")
                    .append(weather.getPrecipitation()).append(" precipitation.\n\n");
            day++;
        }

        promptBuilder.append("The image should include one outfit per day, with no background or human figures. ")
                .append("Keep the layout minimalistic and clean.");

        return promptBuilder.toString();
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
