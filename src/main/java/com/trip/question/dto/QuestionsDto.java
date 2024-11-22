package com.trip.question.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionsDto {
    private int questionId;
    private String title;
    private String createdAt;
    private boolean isAnswered;
}
