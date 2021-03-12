package com.hlushkov.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int id;
    private int movieId;
    private int userId;
    private String text;
}
