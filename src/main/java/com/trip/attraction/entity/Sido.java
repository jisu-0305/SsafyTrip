package com.trip.attraction.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "sidos")
@Getter
public class Sido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Column(unique = true)
    private int sidoCode;
    private String sidoName;
}
