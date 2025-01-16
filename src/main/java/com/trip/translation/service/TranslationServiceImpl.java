package com.trip.translation.service;

import com.trip.translation.dto.TranslationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class TranslationServiceImpl implements TranslationService {

    private final WebClient.Builder webClientBuilder;

    @Value("${deepl.api.key}")
    private String apiKey;

    @Value("${deepl.api.base-url}")
    private String baseUrl;

    @Override
    public String translateKorToEng(String text) {
        WebClient webClient = webClientBuilder.baseUrl(baseUrl).build();

        TranslationResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/translate")
                        .queryParam("auth_key", apiKey)
                        .queryParam("text", text.trim()) // 전달 받은 text를 사용
                        .queryParam("target_lang", "EN") // 기본 언어 EN으로 설정
                        .build())
                .retrieve()
                .bodyToMono(TranslationResponse.class)
                .block();

        return response.getTranslations().get(0).getText(); // 번역 결과 반환
    }
}
