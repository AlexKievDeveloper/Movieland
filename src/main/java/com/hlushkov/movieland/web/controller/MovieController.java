package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.entity.SortDirection;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "movie", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAllMovies(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                     @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.info("Get request for all movies");

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(ratingSortDirection);
        movieRequest.setPriceDirection(priceSortDirection);
        return movieService.findAllMovies(movieRequest);
    }

    @GetMapping("random")
    public List<Movie> findRandomMovies() {
        log.info("Get request for three random movies");
        return movieService.findRandomMovies();
    }

    @GetMapping("genre/{genreId}")
    public List<Movie> findMoviesByGenre(@PathVariable int genreId,
                                         @RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                         @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {
        log.info("Get request for movies by genre");

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(ratingSortDirection);
        movieRequest.setPriceDirection(priceSortDirection);
        return movieService.findMoviesByGenre(genreId, movieRequest);
    }
}
