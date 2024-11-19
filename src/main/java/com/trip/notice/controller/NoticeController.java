package com.trip.notice.controller;

import com.trip.notice.dto.Notice;
import com.trip.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
@Tag(name = "Notice", description = "공지사항 API")
@Slf4j
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

//    @Operation(summary = "모든 공지사항 조회", description = "모든 공지사항 목록을 반환합니다.")
//    public ResponseEntity<List<Notice>> getAllNotices() {
//        List<Notice> notices = noticeService.getAllNotices();
//        return ResponseEntity.ok(notices);
//    }

    @Operation(summary = "모든 공지사항 조회 (페이징)", description = "페이징 처리를 통해 공지사항 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<List<Notice>> getPagedNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<Notice> pagedNotices = noticeService.getPagedNotices(page, size);
        return ResponseEntity.ok(pagedNotices);
    }

    @Operation(summary = "특정 공지사항 조회", description = "ID로 특정 공지사항을 조회합니다.")
    @GetMapping("/{noticeId}")
    public ResponseEntity<Notice> getNoticeById(
            @Parameter(description = "조회할 공지사항의 ID", example = "1")
            @PathVariable int noticeId) {
        Notice notice = noticeService.getNoticeById(noticeId);
        return ResponseEntity.ok(notice);
    }

    @Operation(summary = "공지사항 작성", description = "새로운 공지사항을 작성합니다. 이 기능은 관리자만 사용할 수 있습니다.")
    @PostMapping
    public ResponseEntity<Notice> createNotice(
            @Valid @RequestBody Notice notice, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        notice.setUserId(userId);
        Notice createdNotice = noticeService.createNotice(notice);
        return ResponseEntity.status(201).body(createdNotice);
    }

    @Operation(summary = "공지사항 수정", description = "ID로 특정 공지사항을 수정합니다. 관리자가 접근해야 합니다.")
    @PutMapping("/{noticeId}")
    public ResponseEntity<Notice> updateNotice(
            @Parameter(description = "수정할 공지사항의 ID", example = "1")
            @PathVariable int noticeId,
            @RequestBody Notice noticeDetails) {
        Notice updatedNotice = noticeService.updateNotice(noticeId, noticeDetails);
        return ResponseEntity.ok(updatedNotice);
    }

    @Operation(summary = "공지사항 삭제", description = "ID로 특정 공지사항을 삭제합니다. 관리자가 접근해야 합니다.")
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<String> deleteNotice(
            @Parameter(description = "삭제할 공지사항의 ID", example = "1")
            @PathVariable int noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.ok("공지사항이 성공적으로 삭제되었습니다.");
    }
}
