package com.trip.review.dto;

import com.trip.review.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ReviewResponseDTO {
    private Long reviewId;
    private String email;
    private String title;
    private String content;
    private int hit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String firstImageUrl;

    // DTO → Entity
    public Review toEntity() {
        return Review.builder()
                .email(this.email)
                .title(this.title)
                .content(this.content)
                .build();
    }

    // Entity → DTO
    public static ReviewResponseDTO fromEntity(Review reveiw) {
        return ReviewResponseDTO.builder()
                .reviewId(reveiw.getReviewId())
                .email(reveiw.getEmail())
                .title(reveiw.getTitle())
                .content(reveiw.getContent())
                .hit(reveiw.getHit())
                .createdAt(reveiw.getCreatedAt())
                .updatedAt(reveiw.getUpdatedAt())
                .firstImageUrl(null) // 첫 번째 이미지 URL
                .build();
    }

}
