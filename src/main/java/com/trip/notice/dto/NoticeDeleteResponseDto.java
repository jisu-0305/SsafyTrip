package com.trip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class NoticeDeleteResponseDto {
    private String status;
    private String message;
    
    public static NoticeDeleteResponseDto of() {
        return NoticeDeleteResponseDto.builder()
                .status("success")
                .message("공지사항이 성공적으로 삭제되었습니다")
                .build();
    }
}