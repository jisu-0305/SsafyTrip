package com.trip.question.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionInsertReqDto {
    String title;
    String content;
    public QuestionInsertReqDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
