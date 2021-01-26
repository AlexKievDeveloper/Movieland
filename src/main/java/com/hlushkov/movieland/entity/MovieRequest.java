package com.hlushkov.movieland.entity;

import lombok.Data;

import java.util.Optional;

@Data
public class MovieRequest {
    private Optional<SortDirection> ratingDirection;
    private Optional<SortDirection> priceDirection;
}
