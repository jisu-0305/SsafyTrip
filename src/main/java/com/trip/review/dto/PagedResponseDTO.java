package com.trip.review.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedResponseDTO {
    private List<ReviewResponseDTO> content;    // 개별 리뷰 데이터 리스트
    private int totalPages;     // 전체 페이지 수
    private long totalElements; // 전체 아이템 수
}
