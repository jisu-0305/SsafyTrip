package com.trip.question.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionAnswerReqDto {
    int userId;
    int questionId;
    String content;

    public QuestionAnswerReqDto(int userId, int questionId, String content) {
        this.userId = userId;
        this.questionId = questionId;
        this.content = content;
    }
}
