package com.trip.favorite.service;

import com.trip.attraction.dto.AttractionDto;
import com.trip.attraction.dto.PagedAttractionResponseDto;
import com.trip.favorite.entity.Favorite;
import com.trip.favorite.mapper.FavoriteMapper;
import com.trip.favorite.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;

    public void addFavorite(Long userId, int attractionId) {
        if (favoriteRepository.existsByUserIdAndAttractionId(userId, attractionId)) {
            throw new IllegalStateException("이미 좋아요를 누른 상태입니다.");
        }

        Favorite favorite = Favorite.create(userId, attractionId);
        favoriteRepository.save(favorite);
    }

    @Override
    public void removeFavorite(Long userId, int attractionId) {
        Favorite favorite = favoriteRepository.findByUserIdAndAttractionId(userId, attractionId)
                .orElseThrow(() -> new IllegalStateException("좋아요하지 않은 상태에서 삭제 요청을 보낼 수 없습니다."));

        favoriteRepository.delete(favorite);
    }


    @Override
    public PagedAttractionResponseDto getFavoriteAttractions(Long userId, int page, int size, String word) {
        int offset = (page - 1) * size;

        int totalCount = favoriteMapper.countFavoriteAttractions(userId, word);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        List<AttractionDto> attractionList = favoriteMapper.selectFavoriteAttractions(userId, word, offset, size);

        return new PagedAttractionResponseDto(
                attractionList,
                totalCount,
                totalPages
        );
    }

    @Override
    public boolean isLikedByUser(Long userId, int attractionId) {
        if (userId == null) {
            return false;
        }

        return favoriteRepository.existsByUserIdAndAttractionId(userId, attractionId);
    }
}
