package com.trip.ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
class AiController {
    private final ChatClient chatClient;
    private final ImageModel openaiImageModel; // ImageModel 의존성 주입

    AiController(ChatClient chatClient, ImageModel openaiImageModel) {
        this.chatClient = chatClient;
        this.openaiImageModel = openaiImageModel;
    }

    @GetMapping("/ai")
    public Map<String, String> completion(@RequestParam(value = "message", defaultValue = "코디 이미지 하나 생성해줘.") String message) {
        return Map.of(
                "completion",
                chatClient.prompt()
                        .user(message)
                        .call()
                        .content());
    }

    @GetMapping("/ai/image")
    public ImageResponse generateImage(@RequestParam(value = "prompt", defaultValue = "당신은 사용자가 여행 계획을 세울 때 적합한 옷차림을 추천하는 AI 스타일리스트입니다. 부산 여행을 1박 2일 계획중이고 첫날은 비와 함께 20도의 날씨 이고 시장을 갈 생각이야. 2번째 날은 맑은 날씨와 함께 17도의 온도이고 바다를 갈생각이야. 여기에 맞는 옷 코디 이미지 생성해줘. 그 외에 배경은 그냥 없어도 돼. 딱 옷만. 잘하면 승진 시켜줄게.") String prompt) {

        return openaiImageModel.call(
                new ImagePrompt(prompt,
                        OpenAiImageOptions.builder()
                                .withQuality("hd")
                                .withN(1) // 여기에서 n 값을 1로 설정
                                .withHeight(1024)
                                .withWidth(1024)
                                .build()));
    }
}