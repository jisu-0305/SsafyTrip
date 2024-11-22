package com.trip.comment.controller;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.service.CommentService;
import com.trip.common.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comments")
@Tag(name = "댓글 컨트롤러", description = "댓글 생성 및 삭제 API 제공")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{attractionId}")
    @Operation(summary = "댓글 생성", description = "관광지 ID와 댓글 정보를 통해 댓글을 생성합니다.")
    public ResponseEntity<ResponseDto> createComment(
            @PathVariable("attractionId") int attractionId,
            @RequestBody CommentCreateRequestDto requestDto,
            HttpSession session) {

        // 로그인된 사용자 이메일 가져오기
        String loggedInEmail = (String) session.getAttribute("email");
        if (loggedInEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.failure("로그인이 필요합니다."));
        }

        // 작성자 이메일과 로그인된 이메일 비교
        if (!loggedInEmail.equals(requestDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure("작성자의 이메일이 로그인된 사용자와 일치하지 않습니다."));
        }

        // 댓글 생성
        commentService.createComment(attractionId, requestDto);
        return ResponseEntity.ok(ResponseDto.success("Comment created successfully"));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 ID를 통해 댓글을 삭제합니다.")
    public ResponseEntity<ResponseDto> deleteComment(@PathVariable("commentId") int commentId, HttpSession session) {
        // 로그인된 사용자 이메일 가져오기
        String loggedInEmail = (String) session.getAttribute("email");
        if (loggedInEmail == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseDto.failure("로그인이 필요합니다."));
        }

        // 댓글 삭제
        boolean isDeleted = commentService.deleteComment(commentId, loggedInEmail);
        if (!isDeleted) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ResponseDto.failure("댓글 삭제 권한이 없습니다."));
        }

        return ResponseEntity.ok(ResponseDto.success("Comment deleted successfully"));
    }
}
