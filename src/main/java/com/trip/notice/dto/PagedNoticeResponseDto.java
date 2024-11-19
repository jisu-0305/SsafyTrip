package com.trip.notice.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedNoticeResponseDto {
    private List<NoticeListResponseDto> noticeList;
    private int totalPages;
    private long totalElements; 
    private int pageNumber;
    private int pageSize;
}