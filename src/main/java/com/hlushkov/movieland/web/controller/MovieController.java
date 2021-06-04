package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.common.request.SaveMovieRequest;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.security.annotation.Secured;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {
    private final MovieService movieService;

    @Secured({Role.USER, Role.ADMIN})
    @GetMapping
    public List<Movie> findMovies(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                  @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.debug("Request for all movies received");
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findMovies(findMoviesRequest);
    }

    @Secured({Role.USER, Role.ADMIN})
    @GetMapping("{movieId}")
    public MovieDetails findById(@PathVariable int movieId,
                                 @RequestParam(value = "currency", required = false) String currency) {
        log.debug("Request for movie with id: {} received", movieId);
        return movieService.findById(movieId, Optional.ofNullable(currency));
    }

    @Secured({Role.USER, Role.ADMIN})
    @GetMapping("random")
    public List<Movie> findRandom() {
        log.debug("Request for random movies received");
        return movieService.findRandom();
    }

    @Secured({Role.USER, Role.ADMIN})
    @GetMapping("genre/{genreId}")
    public List<Movie> findByGenre(@PathVariable int genreId,
                                   @RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                   @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {

        log.debug("Request for movies by genre received");
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findByGenre(genreId, findMoviesRequest);
    }

    @Secured({Role.ADMIN})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveMovie(@RequestBody SaveMovieRequest saveMovieRequest) {
        log.info("Request for add movie");
        movieService.saveMovie(saveMovieRequest);
    }

    @Secured({Role.ADMIN})
    @PutMapping("{movieId}")
    public void editMovie(@PathVariable(required = false) Integer movieId, @RequestBody Movie movie) {
        log.info("Request for edit movie");
        movieService.editMovie(movie, movieId);
    }

    @Secured({Role.ADMIN})
    @PutMapping("{movieId}/genres")
    public void editMovieGenres(@PathVariable(required = false) Integer movieId, @RequestBody List<Integer> genreIds) {
        log.info("Request for edit movie genres");
        movieService.editMovieGenres(movieId, genreIds);
    }

    @Secured({Role.ADMIN})
    @PutMapping("{movieId}/countries")
    public void editMovieCountries(@PathVariable(required = false) Integer movieId, @RequestBody List<Integer> countryIds) {
        log.info("Request for edit movie countries");
        movieService.editMovieCountries(movieId, countryIds);
    }

    @Secured({Role.ADMIN})
    @DeleteMapping("{movieId}/reviews")
    public ResponseEntity<Object> removeReviewsByMovieId(@PathVariable(required = false) Integer movieId) {
        log.info("Request for remove reviews by movie id");
        if (movieService.removeReviewsByMovieId(movieId)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
