package com.trip.question.dto;

public class QuestionInsertDto {
    long userId;
    String title;
    String content;
    public QuestionInsertDto(long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
