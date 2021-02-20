package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findMovies(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                  @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.debug("Request for all movies received");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        movieRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findMovies(movieRequest);
    }

    @GetMapping("{movieId}")
    public MovieDetails findMovieById(@PathVariable int movieId) {
        log.debug("Request for movie with id: {} received", movieId);
        return movieService.findMovieDetailsByMovieId(movieId);
    }

    @GetMapping("random")
    public List<Movie> findRandom() {
        log.debug("Request for random movies received");
        return movieService.findRandom();
    }

    @GetMapping("genre/{genreId}")
    public List<Movie> findByGenre(@PathVariable int genreId,
                                   @RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                   @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.debug("Request for movies by genre received");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        movieRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findByGenre(genreId, movieRequest);
    }
}
