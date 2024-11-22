package com.trip.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class NoticeCreateResponseDto {
    private String status;
    private String message;
    
    public static NoticeCreateResponseDto of() {
        return NoticeCreateResponseDto.builder()
                .status("success")
                .message("공지사항이 성공적으로 생성되었습니다.")
                .build();
    }
}