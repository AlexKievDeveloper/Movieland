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
    public String findMovieById() {
        return "SELECT movies.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, movie_description, movie_rating, movie_price, poster_picture_path  " +
                "FROM movies LEFT JOIN posters ON (movies.movie_id=posters.movie_id) WHERE movies.id = ?";
    }

    @Bean
    public String findReviewsByMovieId() {
        return "SELECT reviews.review_id, reviews.user_id, reviews.review_review, users.user_name FROM reviews LEFT JOIN users ON (reviews.user_id = users.user_id)  WHERE movie_id = ?";
    }

    @Bean
    public String findCountryByMovieId() {
        return "SELECT countries.country_id, countries.country_name FROM movies_countries LEFT JOIN countries ON (movies_countries.country_id = countries.country_id) WHERE movie_id = ?;";
    }

    @Bean
    public String findMovieWithDetailsByMovieId() {
        return "SELECT movies_genres.movie_id, movie_name_russian, movie_name_native, movie_year_of_release, " +
                "movie_description, movie_rating, movie_price, poster_picture_path, movies_countries.country_id, " +
                "countries.country_name, movies_genres.genre_id, genres.genre_name, reviews.review_id, reviews.user_id, " +
                "reviews.review_review, users.user_name FROM movies INNER JOIN posters ON (movies.movie_id=posters.movie_id) " +
                "INNER JOIN movies_countries ON (posters.movie_id=movies_countries.movie_id) " +
                "INNER JOIN countries ON (movies_countries.country_id = countries.country_id) " +
                "INNER JOIN movies_genres ON (movies_countries.movie_id=movies_genres.movie_id) " +
                "INNER JOIN genres ON (movies_genres.genre_id = genres.genre_id) " +
                "INNER JOIN reviews ON (movies_genres.movie_id = reviews.movie_id) " +
                "INNER JOIN users ON (reviews.user_id = users.user_id)  WHERE movies.movie_id = ?";
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
}
