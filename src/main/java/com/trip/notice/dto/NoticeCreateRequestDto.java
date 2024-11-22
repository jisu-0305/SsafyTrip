package com.trip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
//import org.hibernate.validator.constraints.URL;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeCreateRequestDto {
    @NotBlank(message = "제목은 필수입니다")
    @Size(min = 2, max = 100, message = "제목은 2-100자 사이여야 합니다")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    private String content;

//    @URL(message = "올바른 URL 형식이어야 합니다")
    private String imageUrl;
    
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}