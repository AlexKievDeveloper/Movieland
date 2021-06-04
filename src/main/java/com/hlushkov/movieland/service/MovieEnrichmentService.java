package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.EnrichmentType;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.entity.Movie;

public interface MovieEnrichmentService {

    MovieDetails enrich(Movie movie, EnrichmentType... enrichmentTypes);

}
