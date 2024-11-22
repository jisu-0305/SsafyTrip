package com.trip.notice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trip.notice.dto.NoticeCreateRequestDto;
import com.trip.notice.dto.NoticeCreateResponseDto;
import com.trip.notice.dto.NoticeDeleteResponseDto;
import com.trip.notice.dto.NoticeListResponseDto;
import com.trip.notice.dto.NoticeUpdateRequestDto;
import com.trip.notice.dto.NoticeUpdateResponseDto;
import com.trip.notice.dto.NoticeDetailDto;
import com.trip.notice.dto.PagedNoticeResponseDto;
import com.trip.notice.service.NoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notices")
@Tag(name = "Notice", description = "공지사항 API")
@Slf4j
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "모든 공지사항 조회 (페이징)", description = "페이징 처리를 통해 공지사항 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<PagedNoticeResponseDto> getPagedNotices(
        @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
        @RequestParam(defaultValue = "1") int page,
        @Parameter(description = "페이지 크기", example = "10")
        @RequestParam(defaultValue = "10") int size,
        @Parameter(description = "검색어 (선택적)", example = "")
        @RequestParam(required = false) String keyword) {  
    
        PagedNoticeResponseDto pagedNotices = noticeService.getPagedNotices(page, size, keyword);
        return ResponseEntity.ok(pagedNotices);
    }
    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDetailDto> getNoticeById(
            @Parameter(description = "조회할 공지사항의 ID", example = "1")
            @PathVariable int noticeId) {
        NoticeDetailDto notice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(notice);
    }

    @Operation(summary = "공지사항 작성", description = "새로운 공지사항을 작성합니다.")
    @PostMapping
    public ResponseEntity<NoticeCreateResponseDto> createNotice(
            @Valid @RequestBody NoticeCreateRequestDto requestDto, 
            HttpSession session) {
        //수정 필요
        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }
        
        NoticeCreateResponseDto response = noticeService.createNotice(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "공지사항 수정", description = "ID로 특정 공지사항을 수정합니다.")
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeUpdateResponseDto> updateNotice(
            @Parameter(description = "수정할 공지사항의 ID", example = "1")
            @PathVariable int noticeId,
            @Valid @RequestBody NoticeUpdateRequestDto requestDto,
            HttpSession session) {

        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }

        NoticeUpdateResponseDto updatedNotice = noticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(updatedNotice);
    }

    @Operation(summary = "공지사항 삭제",
            description = "ID로 특정 공지사항을 삭제합니다. 삭제를 위해 로그인이 필요합니다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<NoticeDeleteResponseDto> deleteNotice(
            @Parameter(description = "삭제할 공지사항의 ID", example = "1")
            @PathVariable int noticeId,
            HttpSession session) {

        String loggedInEmail = (String) session.getAttribute("email");
//        if (loggedInEmail == null) {
//            throw new RuntimeException("로그인이 필요합니다");
//        }
        
        NoticeDeleteResponseDto response = noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok(response);
    }
}