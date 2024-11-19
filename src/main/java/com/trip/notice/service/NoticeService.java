package com.trip.notice.service;

import com.trip.notice.dto.*;

public interface NoticeService {
    PagedNoticeResponseDto getPagedNotices(int page, int size, String keyword);
    NoticeDetailDto getNoticeById(int noticeId);
    NoticeCreateResponseDto createNotice(NoticeCreateRequestDto notice);
    NoticeUpdateResponseDto updateNotice(int noticeId, NoticeUpdateRequestDto noticeDetails);
    NoticeDeleteResponseDto deleteNotice(int noticeId);
}
