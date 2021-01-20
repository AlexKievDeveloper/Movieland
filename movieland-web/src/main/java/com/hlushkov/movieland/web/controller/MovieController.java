package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.entity.MovieRequest;
import com.hlushkov.movieland.entity.SortDirection;
import com.hlushkov.movieland.service.MovieService;
import com.hlushkov.movieland.entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private final MovieService movieService;

    @GetMapping("movie")
    public List<Movie> getAllMovies(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                    @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(ratingSortDirection);
        movieRequest.setPriceDirection(priceSortDirection);
        log.info("Get request for all movies");
        log.info("Movies list: {}", movieService.getAllMovies(movieRequest));
        return movieService.getAllMovies(movieRequest);
    }

    @GetMapping("random")
    public List<Movie> getThreeRandomMovies() {
        log.info("Get request for three random movies");
        return movieService.getThreeRandomMovies();
    }

    @GetMapping("movie/genre/{genreId}")
    public List<Movie> getMoviesByGenre(@PathVariable int genreId) {
        log.info("Get request for movies by genre");
        return movieService.getMoviesByGenre(genreId);
    }
}
