package com.trip.attraction.dto;

import com.trip.attraction.entity.GuGun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GuGunDto {
    private int gugunCode;
    private String gugunName;

    public static GuGunDto fromEntity(GuGun guGun) {
        return new GuGunDto(guGun.getGugunCode(), guGun.getGugunName());
    }
}