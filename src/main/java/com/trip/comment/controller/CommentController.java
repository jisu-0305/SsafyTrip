package com.trip.comment.controller;

import com.trip.comment.dto.CommentCreateRequestDto;
import com.trip.comment.dto.CommentDeleteResponseDto;
import com.trip.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> createComment(
            @PathVariable("attractionId") int attractionId,
            @RequestBody CommentCreateRequestDto requestDto) {

        commentService.createComment(attractionId, requestDto);
        return ResponseEntity.ok(new CommentDeleteResponseDto("success", "Comment created successfully"));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 ID를 통해 댓글을 삭제합니다.")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") int commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(new CommentDeleteResponseDto("success", "Comment deleted successfully"));
    }
}
