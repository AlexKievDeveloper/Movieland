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


        log.info("Get request for all movies");

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(ratingSortDirection);
        movieRequest.setPriceDirection(priceSortDirection);

        return movieService.getAllMovies(movieRequest);
    }

    @GetMapping("random")
    public List<Movie> getRandomMovies() {
        log.info("Get request for three random movies");
        return movieService.getRandomMovies();
    }

    @GetMapping("movie/genre/{genreId}")
    public List<Movie> getMoviesByGenre(@PathVariable int genreId,
                                        @RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                        @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {
        log.info("Get request for movies by genre");

        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(ratingSortDirection);
        movieRequest.setPriceDirection(priceSortDirection);
        return movieService.getMoviesByGenre(genreId, movieRequest);
    }
}
