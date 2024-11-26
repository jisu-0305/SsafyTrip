package com.trip.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.ai.dto.WeatherDto;
import com.trip.attraction.service.AttractionService;
import com.trip.schedule.dto.ScheduleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AttractionService attractionService;
    private final ChatClient chatClient;
    private final ObjectMapper jacksonObjectMapper;

    @Override
    public List<WeatherDto> getWeatherList(ScheduleDetailDto scheduleDetail) {
        String prompt = generatePrompt(scheduleDetail);
        String jsonResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        String cleanedJsonResponse = extractPureJson(jsonResponse);

        try {
            return jacksonObjectMapper.readValue(jsonResponse, new TypeReference<List<WeatherDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data from AI response", e);
        }
    }

    private String generatePrompt(ScheduleDetailDto scheduleDetail) {
        Map<String, List<String>> titlesByDate = attractionService.getTitlesByDate(scheduleDetail);
        StringBuilder promptBuilder = new StringBuilder();

        // Schedule Header
        promptBuilder.append("Please extract the locations from the provided schedule for each date, ")
                .append("combine the locations for each date into a single string separated by commas, and ")
                .append("return the weather information as a JSON array that matches the following Java DTO class:\n\n");

        // Define WeatherDto class
        promptBuilder.append("public class WeatherDto {\n")
                .append("    private String date; // Date in the format 'YYYY-MM-DD'\n")
                .append("    private String location; // Combined locations for the date\n")
                .append("    private double highTemperature; // High temperature in Celsius\n")
                .append("    private double lowTemperature; // Low temperature in Celsius\n")
                .append("    private String weatherCondition; // e.g., '맑음', '흐림'\n")
                .append("    private String precipitation; // e.g., '10mm'\n")
                .append("}\n\n");

        // Add start date
        promptBuilder.append("**Start date: ")
                .append(scheduleDetail.getSchedule().getStartDate())
                .append("**\n\n");

        // Add schedule details with combined locations
        promptBuilder.append("Schedule:\n");
        titlesByDate.forEach((date, titles) -> {
            promptBuilder.append(date).append(" day:\n")
                    .append("- Locations: ").append(String.join(", ", titles)).append("\n");
        });

        // Instructions for expected output
        promptBuilder.append("\nReturn the data as a JSON array matching the WeatherDto class above. ")
                .append("Do not include any additional text or explanation, only the JSON array. For example:\n\n")
                .append("[\n")
                .append("    {\n")
                .append("        \"date\": \"2024-12-01\",\n")
                .append("        \"location\": \"힐사이드호텔, 힐 사이드 관광호텔\",\n")
                .append("        \"highTemperature\": 10.0,\n")
                .append("        \"lowTemperature\": 5.0,\n")
                .append("        \"weatherCondition\": \"흐림\",\n")
                .append("        \"precipitation\": \"2mm\"\n")
                .append("    },\n")
                .append("    ...\n")
                .append("]\n");

        return promptBuilder.toString();
    }

    // 순수 JSON 추출 로직
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


