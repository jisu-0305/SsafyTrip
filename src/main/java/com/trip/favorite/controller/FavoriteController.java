package com.trip.favorite.controller;

import com.trip.common.ResponseDto;
import com.trip.favorite.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
