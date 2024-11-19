package com.trip.notice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder

public class Notice {
    private int noticeId; // 공지사항 고유 번호
    private String title; // 제목
    private String content; // 내용
    private String userId; // 작성자 ID
    private LocalDateTime createdAt; // 생성 시간
    private LocalDateTime updatedAt; // 수정 시간
    private String imageUrl; // 이미지 URL

    public Notice() {}

    public Notice(int noticeId, String title, String content, String userId, LocalDateTime createdAt, LocalDateTime updatedAt, String imageUrl) {
        this.noticeId = noticeId;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.imageUrl = imageUrl;
    }
}
