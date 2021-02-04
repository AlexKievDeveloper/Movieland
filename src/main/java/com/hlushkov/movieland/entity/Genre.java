package com.hlushkov.movieland.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Genre {
    private int id;
    private String name;
}
