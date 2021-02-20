package com.hlushkov.movieland.common.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private int movieId;
    private String text;
}
