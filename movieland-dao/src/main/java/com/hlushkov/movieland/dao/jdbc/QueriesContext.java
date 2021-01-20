package com.hlushkov.movieland.dao.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueriesContext {
    /**
     * JdbcMovieDao queries
     */
    @Bean
    public String getAllMovies() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id);";
    }

    @Bean
    public String getGetMoviesByGenre() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";
    }

    @Bean
    public String getAllMoviesSortedByRating() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY rating DESC";
    }

    @Bean
    public String getAllMoviesSortedByDescPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price DESC";
    }

    @Bean
    public String getAllMoviesSortedByAcsPrice() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id) ORDER BY price ASC";
    }

    /**
     * JdbcGenreDao queries
     */

    @Bean
    public String getAllGenres() {
        return "SELECT genres.genre_id, genres.name FROM genres;";
    }
}
