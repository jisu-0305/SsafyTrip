package com.trip.attraction.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApiDetailVo {
    String name;
    String text;
    public ApiDetailVo(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
