package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.security.annotation.Secured;
import com.hlushkov.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "genre", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController {
    private final GenreService genreService;

    @Secured({Role.USER, Role.ADMIN})
    @GetMapping
    public List<Genre> findAll() {
        log.debug("Request for all genres received");
        return genreService.findAll();
    }

//    @Secured({Role.USER, Role.ADMIN})
//    @PutMapping
//    public void updateMoviesGenresByMovieId(@PathVariable int movieId, @RequestBody List<Review> reviews) {
//        log.debug("Request to add a reviews received");
//        genreService.updateMoviesGenresByMovieId(movieId, reviews);
//    }
}
