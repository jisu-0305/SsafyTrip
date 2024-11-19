package com.trip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NoticeListResponseDto {
    private int noticeId;
    private String title;
    private LocalDateTime createdAt;
}