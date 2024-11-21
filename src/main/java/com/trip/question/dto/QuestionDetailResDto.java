package com.trip.question.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionDetailResDto {
    private int questionId;  // 질문 고유 ID
    private String title;     // 질문 제목
    private String content;   // 질문 내용
    private LocalDate createdAt; // 질문 생성 시간
    private boolean isAnswered;      // 답변 여부
}
