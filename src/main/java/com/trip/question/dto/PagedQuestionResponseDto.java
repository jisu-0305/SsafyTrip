package com.trip.question.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class PagedQuestionResponseDto {
    List<QuestionsDto> questionsList;
    int totalCount;
    int totalPages;
    PagedQuestionResponseDto(List<QuestionsDto> questionsList, int totalCount, int totalPages) {
        this.questionsList = questionsList;
        this.totalCount = totalCount;
        this.totalPages = totalPages;
    }
}
