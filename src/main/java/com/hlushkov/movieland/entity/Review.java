package com.hlushkov.movieland.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {
    private int id;
    private int movieId;
    private User user;
    private String text;
}
