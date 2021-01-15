package com.hlushkov.web.controller;

import com.hlushkov.service.MovieService;
import entity.Movie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private final MovieService movieService;

    @GetMapping("movie")
    public List<Movie> getAllMovies(){
        log.info("Get request for all movies");
        return movieService.getAllMovies();
    }

    @GetMapping("random")
    public List<Movie> getThreeRandomMovies(){
        log.info("Get request for three random movies");
        return movieService.getThreeRandomMovies();
    }
}
