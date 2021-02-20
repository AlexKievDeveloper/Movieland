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
        return "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, movie_description, movie_rating, movie_price, poster_picture_path FROM movies " +
                "LEFT JOIN posters ON (movies.movie_id=posters.movie_id)";
    }

    @Bean
    public String findRandomMovies() {
        return "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, movie_description, movie_rating, movie_price, poster_picture_path " +
                " FROM movies LEFT JOIN posters ON (movies.movie_id=posters.movie_id)" +
                " ORDER BY random() limit ?";
    }

    @Bean
    public String findMoviesByGenre() {
        return "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, movie_description, movie_rating, movie_price, poster_picture_path  " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id = posters.movie_id) " +
                "LEFT JOIN movies_genres ON (movies.movie_id = movies_genres.movie_id) " +
                "WHERE movies_genres.genre_id = ?";
    }

    @Bean
    public String findMovieWithDetailsByMovieId() {
        return "SELECT movies_genres.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, poster_picture_path, movies_countries.country_id, " +
                "countries.country_name, movies_genres.genre_id, genres.genre_name, reviews.review_id, reviews.user_id, " +
                "reviews.review_text, users.user_nickname FROM movies LEFT JOIN posters ON (movies.movie_id=posters.movie_id) " +
                "LEFT JOIN movies_countries ON (posters.movie_id=movies_countries.movie_id) " +
                "LEFT JOIN countries ON (movies_countries.country_id = countries.country_id) " +
                "LEFT JOIN movies_genres ON (movies_countries.movie_id=movies_genres.movie_id) " +
                "LEFT JOIN genres ON (movies_genres.genre_id = genres.genre_id) " +
                "LEFT JOIN reviews ON (movies_genres.movie_id = reviews.movie_id) " +
                "LEFT JOIN users ON (reviews.user_id = users.user_id)  WHERE movies.movie_id = ?";
    }

    /**
     * JdbcGenreDao queries
     */

    @Bean
    public String findAllGenres() {
        return "SELECT genres.genre_id, genres.genre_name FROM genres;";
    }

    @Bean
    public String findGenreByMovieId() {
        return "SELECT genres.genre_id, genres.genre_name FROM movies_genres LEFT JOIN genres ON (movies_genres.genre_id = genres.genre_id) WHERE movie_id = ?;";
    }

    /**
     * JdbcUserDao queries
     */

    @Bean
    public String findUserByEmail() {
        return "SELECT user_id, user_nickname, user_email, user_password, user_salt, user_role FROM users WHERE user_email = ?";
    }

    /**
     * JdbcReviewDao queries
     */

    @Bean
    public String addReview() {
        return "INSERT INTO reviews (user_id, movie_id, review_text) VALUES (:user_id, :movie_id, :text)";
    }

}
