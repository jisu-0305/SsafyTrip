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
    String title;
    String content;
    public ApiDetailVo(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
