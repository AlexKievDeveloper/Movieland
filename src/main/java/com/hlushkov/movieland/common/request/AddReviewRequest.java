package com.hlushkov.movieland.common.request;

import lombok.Data;

@Data
public class AddReviewRequest {
    private int movieId;
    private String text;
}
