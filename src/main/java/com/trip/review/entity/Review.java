package com.trip.review.entity;

import com.trip.review.dto.ReviewRequestDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId;

    @Column(name = "email")
    private String email;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "hit")
    private int hit;

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 시간

    // insert 시에만 실행됨.
    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now(); // 현재 시간 설정
        }

        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    // DTO → Entity
    public static Review fromRequestDtoToEntity(ReviewRequestDTO dto) {
        return Review.builder()
                .email(dto.getEmail())
                .title(dto.getTitle())
                .content(dto.getContent())
                .hit(0) // 조회수 초기값
                .createdAt(LocalDateTime.now()) // 생성 시간 설정
                .updatedAt(LocalDateTime.now()) // 수정 시간 초기화
                .build();
    }

    // update
    public void update(ReviewRequestDTO dto) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void addHit(){
        this.hit++;
    }
}
