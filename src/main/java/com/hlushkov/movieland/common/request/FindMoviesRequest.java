package com.hlushkov.movieland.common.request;

import com.hlushkov.movieland.common.SortDirection;
import lombok.Data;

import java.util.Optional;

@Data
public class FindMoviesRequest {
    private Optional<SortDirection> ratingDirection;
    private Optional<SortDirection> priceDirection;
}
