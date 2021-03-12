package com.hlushkov.movieland.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbuCurrencyRate {
    private Double rate;
    @JsonProperty("cc")
    private String name;
}

