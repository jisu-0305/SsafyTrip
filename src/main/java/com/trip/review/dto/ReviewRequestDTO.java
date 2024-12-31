package com.trip.review.dto;

import com.trip.review.entity.Review;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReviewRequestDTO {
    private String email;
    private String title;
    private String content;

    public Review toEntity() {
        return Review.builder()
                .email(this.email)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
