package com.trip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeUpdateResponseDto {
    private String status;
    private String message;
    
    public static NoticeUpdateResponseDto of() {
        return NoticeUpdateResponseDto.builder()
                .status("success")
                .message("공지사항이 성공적으로 수정되었습니다")
                .build();
    }
}