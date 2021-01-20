package com.hlushkov.movieland.dao.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueriesContext {
    /**
     * JdbcMovieDao queries
     */
    @Bean
    public String findAllMovies() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id);";
    }

    @Bean
    public String findMoviesByGenre() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";
    }

    @Bean
    public String findAllMoviesSortedByRating() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY rating DESC";
    }

    @Bean
    public String findAllMoviesSortedByDescPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price DESC";
    }

    @Bean
    public String findAllMoviesSortedByAcsPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price ASC";
    }

    @Bean
    public String findAllMoviesByGenreSortedByRating() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY rating DESC";
    }

    @Bean
    public String findAllMoviesByGenreSortedByDescPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY price DESC";
    }

    @Bean
    public String findAllMoviesByGenreSortedByAcsPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ? ORDER BY price ASC";
    }

    /**
     * JdbcGenreDao queries
     */

    @Bean
    public String findAllGenres() {
        return "SELECT genres.genre_id, genres.name FROM genres;";
    }
}
