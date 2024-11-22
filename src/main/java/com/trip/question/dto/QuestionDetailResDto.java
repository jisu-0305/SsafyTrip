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
    // 질문 정보
    private int questionId;
    private String questionTitle;
    private String questionContent;
    private LocalDate questionCreatedAt;
    private boolean questionIsAnswered;
    private String questionAuthorEmail;

    // 답변 정보 (선택적)
    private Integer answerId; // 답변이 없을 수 있으므로 Integer 사용
    private String answerContent;
    private LocalDate answerCreatedAt;
    private String answerAuthorEmail;
}
