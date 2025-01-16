package com.trip.translation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Translation {
    @JsonProperty("detected_source_language")
    private String detectedSourceLanguage;
    private String text;
}
