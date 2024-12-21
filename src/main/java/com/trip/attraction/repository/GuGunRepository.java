package com.trip.attraction.repository;

import com.trip.attraction.entity.GuGun;
import com.trip.attraction.entity.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuGunRepository extends JpaRepository<GuGun, Integer> {
    List<GuGun> findBySidoCode(int sidoCode);
}
