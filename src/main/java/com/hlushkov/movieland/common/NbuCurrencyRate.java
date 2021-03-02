package com.hlushkov.movieland.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NbuCurrencyRate {
    private Double rate;
    private String cc;
}

