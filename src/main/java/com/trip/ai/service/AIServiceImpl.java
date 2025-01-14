package com.trip.ai.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trip.ai.dto.WeatherAndClothesResponseDto;
import com.trip.ai.dto.WeatherDto;
import com.trip.attraction.service.AttractionService;
import com.trip.review.dto.S3ResponseDTO;
import com.trip.review.util.S3Util;
import com.trip.schedule.dto.ScheduleDetailDto;
import com.trip.translation.service.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.Image;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.stabilityai.StabilityAiImageModel;
import org.springframework.ai.stabilityai.api.StabilityAiImageOptions;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AIServiceImpl implements AIService {

    private final AttractionService attractionService;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final TranslationService translationService;
    private final StabilityAiImageModel stabilityAiImageModel;
    private final S3Util s3Util;
    private final String FOLDER_NAME = "aiclothe";

    @Override
    public WeatherAndClothesResponseDto getWeatherAndClothes(ScheduleDetailDto scheduleDetail) {
        // 1. WeatherDto 리스트 생성
        List<WeatherDto> weatherList = getWeatherList(scheduleDetail);

        // 2. 코디 프롬프트 생성
        String korClothesPrompt = generateClothesPrompt(weatherList);

        // 3. 번역 진행
        String engClothesPrompt = translationService.translateKorToEng(korClothesPrompt);

        // 4. 코디 이미지 생성 및 URL 추출
        String clothesURL = generateImageURL(engClothesPrompt);

        // 5. 준비물 DTO 생성 및 추출
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

        try {
            return objectMapper.readValue(cleanedJsonResponse, new TypeReference<List<WeatherDto>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data from AI response", e);
        }
    }

    private String generateWeatherPrompt(ScheduleDetailDto scheduleDetail) {
        Map<String, List<String>> titlesByDate = attractionService. getTitlesByDate(scheduleDetail);
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

        promptBuilder.append("Create a high-resolution image with outfits divided into equal sections, one for each day of the trip. Each section contains exactly one flat-lay arrangement of clothing and accessories based on the weather and location provided in the JSON data. No text, people, or unnecessary background images—only neatly arranged clothing and accessories for each day.\n\n");

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

        promptBuilder.append("Create a coordinate book image based on the following JSON data.");
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

        byte[] imageBytes = new byte[0];

        if (image.getB64Json() != null) {
            // Base64 데이터를 디코딩 -> 여기서 s3 또는 redis를 통한 이미지 관리가 필요할듯함
            imageBytes = Base64.getDecoder().decode(image.getB64Json());
        }

        // S3에 업로드
        String fileName = "ai_generated_" + System.currentTimeMillis() + ".png";

        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            S3ResponseDTO s3ResponseDTO = s3Util.imageUpload(inputStream, FOLDER_NAME, fileName, imageBytes.length, "image/png");

            // S3 URL 반환
            return s3ResponseDTO.getS3Url();
        } catch (IOException e) {
            return null;
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
