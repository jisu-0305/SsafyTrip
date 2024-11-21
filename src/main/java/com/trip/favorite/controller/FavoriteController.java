package com.trip.favorite.controller;

import com.trip.common.ResponseDto;
import com.trip.favorite.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/{attractId}")
    public ResponseEntity<ResponseDto> addFavorite(
            @PathVariable("attractId") int attractId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ResponseDto.failure("로그인이 필요합니다."));
        }

        favoriteService.addFavorite(userId, attractId);
        return ResponseEntity.status(201).body(ResponseDto.success("Favorite added successfully."));
    }

    @DeleteMapping("/{attractId}")
    public ResponseEntity<ResponseDto> removeFavorite(
            @PathVariable("attractId") int attractId,
            @RequestParam("numId") Long numId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(ResponseDto.failure("로그인이 필요합니다."));
        }

        favoriteService.removeFavorite(userId, attractId, numId);
        return ResponseEntity.ok(ResponseDto.success("Favorite removed successfully."));
    }
}
