package com.hlushkov.dao.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueriesContext {
    /**
     * JdbcMovieDao queries
     */
    @Bean
    public String getAllMovies() {
        return "SELECT movies.movie_id, nameRussian, nameNative, yearOfRelease, rating, price, picturePath FROM movies LEFT JOIN posters ON (movies.movie_id=posters.movie_id);";
    }
}
