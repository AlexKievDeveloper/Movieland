package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.EnrichmentType;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CountryService;
import com.hlushkov.movieland.service.GenreService;
import com.hlushkov.movieland.service.MovieEnrichmentService;
import com.hlushkov.movieland.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultMovieEnrichmentService implements MovieEnrichmentService {
    private final GenreService genreService;
    private final CountryService countryService;
    private final ReviewService reviewService;

    @Override
    public MovieDetails enrich(Movie movie, EnrichmentType... enrichmentTypes) {
        MovieDetails movieDetails = MovieDetails.builder().build();
        movieDetails.setId(movie.getId());
        movieDetails.setNameNative(movie.getNameNative());
        movieDetails.setNameRussian(movie.getNameRussian());
        movieDetails.setYearOfRelease(movie.getYearOfRelease());
        movieDetails.setDescription(movie.getDescription());
        movieDetails.setPrice(movie.getPrice());
        movieDetails.setRating(movie.getRating());
        movieDetails.setPicturePath(movie.getPicturePath());
        movieDetails.setGenres(genreService.findGenreByMovieId(movie.getId()));
        movieDetails.setCountries(countryService.findCountriesByMovieId(movie.getId()));
        movieDetails.setReviews(reviewService.findReviewsByMovieId(movie.getId()));
        return movieDetails;
    }

}
