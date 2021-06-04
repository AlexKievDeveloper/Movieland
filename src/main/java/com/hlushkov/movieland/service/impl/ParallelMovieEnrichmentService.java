package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.EnrichmentType;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.CountryService;
import com.hlushkov.movieland.service.GenreService;
import com.hlushkov.movieland.service.MovieEnrichmentService;
import com.hlushkov.movieland.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
@Primary
public class ParallelMovieEnrichmentService implements MovieEnrichmentService {
    private final GenreService genreService;
    private final CountryService countryService;
    private final ReviewService reviewService;

    @Setter(AccessLevel.PACKAGE)
    @Value("${movie.enrichment.timeout}")
    private int timeout;

    @SneakyThrows
    @Override
    public MovieDetails enrich(Movie movie, EnrichmentType... enrichmentTypes) {
        MovieDetails movieDetails = MovieDetails.builder()
                .id(movie.getId())
                .nameNative(movie.getNameNative())
                .nameRussian(movie.getNameRussian())
                .yearOfRelease(movie.getYearOfRelease())
                .rating(movie.getRating())
                .price(movie.getPrice())
                .picturePath(movie.getPicturePath())
                .description(movie.getDescription())
                .build();

        List<CompletableFuture<Void>> completableFutureList = new ArrayList<>();

        for (EnrichmentType enrichmentType : enrichmentTypes) {
            if ("GENRES".equals(enrichmentType.name())) {
                CompletableFuture<Void> enrichWithGenres = CompletableFuture.supplyAsync(() ->
                        genreService.findGenreByMovieId(movie.getId())).thenAccept(movieDetails::setGenres);
                completableFutureList.add(enrichWithGenres);
            }
            if ("COUNTRIES".equals(enrichmentType.name())) {
                CompletableFuture<Void> enrichWithCountries = CompletableFuture.supplyAsync(() ->
                        countryService.findCountriesByMovieId(movie.getId())).thenAccept(movieDetails::setCountries);
                completableFutureList.add(enrichWithCountries);
            }
            if ("REVIEWS".equals(enrichmentType.name())) {
                CompletableFuture<Void> enrichWithReviews = CompletableFuture.supplyAsync(() ->
                        reviewService.findReviewsByMovieId(movie.getId())).thenAccept(movieDetails::setReviews);
                completableFutureList.add(enrichWithReviews);
            }
        }

        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]))
                .exceptionally(this::exceptionHandler)
                .completeOnTimeout(null, timeout, TimeUnit.SECONDS)
                .join();

        return movieDetails;
    }

    private Void exceptionHandler(Throwable e) {
        log.info("Exception during movie details enrichment: ", e);
        return null;
    }

}

