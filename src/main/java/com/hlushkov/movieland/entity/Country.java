package com.hlushkov.movieland.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Country {
    private int id;
    private String name;
}
