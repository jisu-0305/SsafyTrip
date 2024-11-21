package com.trip.favorite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoriteUserResponseDto {
    private int favoriteId;      // 찜 고유 ID
    private int attractionId;    // 관광명소 ID
}
