package com.hlushkov.movieland.request;

import com.hlushkov.movieland.common.SortDirection;
import lombok.Data;

import java.util.Optional;

@Data
public class MovieRequest {
    private Optional<SortDirection> ratingDirection;
    private Optional<SortDirection> priceDirection;
}
