package com.trip.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseDto {
    private String status;
    private String message;

    public static ResponseDto success(String message) {
        return ResponseDto.builder()
                .status("success")
                .message(message)
                .build();
    }

    public static ResponseDto failure(String message) {
        return ResponseDto.builder()
                .status("failure")
                .message(message)
                .build();
    }
}