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
        return "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath FROM movies " +
                "LEFT JOIN posters ON (movies.id=posters.movie_id)";
    }

    @Bean
    public String findRandomMovies() {
        return "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath" +
                " FROM movies LEFT JOIN posters ON (movies.id=posters.movie_id)" +
                " ORDER BY random() limit ?";
    }

    @Bean
    public String findMoviesByGenre() {
        return "SELECT movies.id, nameRussian, nameNative, yearOfRelease, description, rating, price, picturePath " +
                "FROM movies LEFT JOIN posters ON (movies.id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";
    }

    /**
     * JdbcGenreDao queries
     */

    @Bean
    public String findAllGenres() {
        return "SELECT genres.id, genres.name FROM genres;";
    }
}
