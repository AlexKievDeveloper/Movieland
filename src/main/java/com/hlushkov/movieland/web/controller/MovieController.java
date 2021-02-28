package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Currency;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.CreateUpdateMovieRequest;
import com.hlushkov.movieland.common.request.MovieRequest;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.security.annotation.Secure;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @Secure({Role.USER, Role.ADMIN})
    @GetMapping
    public List<Movie> findMovies(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                  @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.debug("Request for all movies received");
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        movieRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findMovies(movieRequest);
    }

    @Secure({Role.USER, Role.ADMIN})
    @GetMapping("{movieId}")
    public MovieDetails findMovieById(@PathVariable int movieId,
                                      @RequestParam(value = "currency", required = false) Currency currency) {
        log.debug("Request for movie with id: {} received", movieId);
        return movieService.findMovieDetailsByMovieId(movieId, Optional.ofNullable(currency));
    }

    @Secure({Role.USER, Role.ADMIN})
    @GetMapping("random")
    public List<Movie> findRandom() {
        log.debug("Request for random movies received");
        return movieService.findRandom();
    }

    @Secure({Role.USER, Role.ADMIN})
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

    @Secure({Role.ADMIN})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void addMovie(@RequestBody CreateUpdateMovieRequest createUpdateMovieRequest) {
        log.info("Request for add movie");
        movieService.addMovie(createUpdateMovieRequest);
    }

    @Secure({Role.ADMIN})
    @PutMapping("{movieId}")
    public void editMovie(@PathVariable int movieId, @RequestBody CreateUpdateMovieRequest createUpdateMovieRequest) {
        log.info("Request for edit movie");
        movieService.editMovie(movieId, createUpdateMovieRequest);
    }

}
