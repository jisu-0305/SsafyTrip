package com.trip.board.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Board {
    private int articleNo;
    private String userId;
    private String subject;
    private String content;
    private int hit;
    private LocalDateTime registerTime;

    public Board() {}

    public Board(int articleNo, String userId, String subject, String content, int hit, LocalDateTime registerTime) {
        this.articleNo = articleNo;
        this.userId = userId;
        this.subject = subject;
        this.content = content;
        this.hit = hit;
        this.registerTime = registerTime;
    }
}
