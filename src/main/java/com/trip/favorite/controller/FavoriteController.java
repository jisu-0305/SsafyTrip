package com.trip.favorite.controller;

import com.trip.attraction.dto.AttractionDto;
import com.trip.common.ResponseDto;
import com.trip.favorite.service.FavoriteService;
import com.trip.global.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{attractionId}")
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
    public ResponseEntity<List<AttractionDto>> getFavoriteAttractions(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        // 좋아요한 관광명소 리스트 가져오기
        List<AttractionDto> favoriteAttractions = favoriteService.getFavoriteAttractions(userId);

        return ResponseEntity.ok(favoriteAttractions);
    }
}
