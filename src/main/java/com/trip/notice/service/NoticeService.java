package com.trip.notice.service;

import com.trip.notice.dto.*;
import com.trip.review.dto.S3ResponseDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

public interface NoticeService {
    PagedNoticeResponseDto getPagedNotices(int page, int size, String keyword);
    NoticeDetailDto getNoticeById(int noticeId);
    NoticeCreateResponseDto createNotice(NoticeCreateRequestDto notice);
    NoticeUpdateResponseDto updateNotice(int noticeId, NoticeUpdateRequestDto noticeDetails);
    NoticeDeleteResponseDto deleteNotice(int noticeId);
    S3ResponseDTO uploadImage(MultipartFile request);
    void deleteImage(HttpSession session);
}
