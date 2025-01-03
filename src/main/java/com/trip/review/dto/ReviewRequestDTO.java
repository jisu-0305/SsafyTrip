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

}
