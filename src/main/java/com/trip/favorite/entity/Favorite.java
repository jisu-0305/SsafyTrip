package com.trip.favorite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorites")
@Getter
@NoArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "attraction_id", nullable = false)
    private int attractionId;

    public static Favorite create(Long userId, int attractionId) {
        Favorite favorite = new Favorite();
        favorite.userId = userId;
        favorite.attractionId = attractionId;
        return favorite;
    }
}
