package com.trip.favorite.controller;

import com.trip.attraction.dto.AttractionDto;
import com.trip.attraction.dto.PagedAttractionResponseDto;
import com.trip.common.ResponseDto;
import com.trip.favorite.service.FavoriteService;
import com.trip.global.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
@Tag(name = "Favorite Controller", description = "관광지 좋아요 관련 API를 제공합니다.")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{attractionId}")
    @Operation(summary = "좋아요 추가", description = "로그인한 사용자가 특정 관광지에 좋아요를 추가합니다.")
    public ResponseEntity<ResponseDto> addFavorite(
            @PathVariable("attractionId") int attractionId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ResponseDto.failure("로그인이 필요합니다."));
        }

        favoriteService.addFavorite(userId, attractionId);
        return ResponseEntity.status(201).body(ResponseDto.success("Favorite added successfully."));
    }

    @DeleteMapping("/{attractionId}")
    @Operation(summary = "좋아요 삭제", description = "로그인한 사용자가 특정 관광지에 좋아요를 삭제합니다.")
    public ResponseEntity<ResponseDto> removeFavorite(
            @PathVariable("attractionId") int attractionId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ResponseDto.failure("로그인이 필요합니다."));
        }

        favoriteService.removeFavorite(userId, attractionId);
        return ResponseEntity.ok(ResponseDto.success("Favorite removed successfully."));
    }

    @GetMapping
    @Operation(summary = "좋아요 목록 조회", description = "로그인한 사용자가 좋아요를 누른 관광지의 상세 정보를 반환합니다. 페이징과 검색 기능을 제공합니다.")
    public ResponseEntity<PagedAttractionResponseDto> getFavoriteAttractions(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "word", required = false) String word,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        PagedAttractionResponseDto pagedAttractionResponseDto = favoriteService.getFavoriteAttractions(userId, page, size, word);

        return ResponseEntity.ok(pagedAttractionResponseDto);
    }
}
