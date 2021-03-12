package com.hlushkov.movieland.dao.jdbc.extractor;

import com.hlushkov.movieland.common.dto.MovieDetails;
import com.hlushkov.movieland.entity.Country;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.entity.Review;
import com.hlushkov.movieland.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MovieDetailsResultSetExtractor implements ResultSetExtractor<MovieDetails> {

    @Override
    public MovieDetails extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        if (!resultSet.next()) {
            throw new IllegalArgumentException("Returned empty result in response to query for Movie with details.");
        }

        MovieDetails movieWithDetails = MovieDetails.builder()
                .id(resultSet.getInt("movie_id"))
                .nameRussian(resultSet.getString("movie_name_russian"))
                .nameNative(resultSet.getString("movie_name_native"))
                .description(resultSet.getString("movie_description"))
                .yearOfRelease(resultSet.getInt("movie_year_of_release"))
                .rating(resultSet.getDouble("movie_rating"))
                .price(resultSet.getDouble("movie_price"))
                .picturePath(resultSet.getString("movie_picture_path"))
                .build();

        Genre firstGenre = Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("genre_name"))
                .build();
        Country firstCountry = Country.builder()
                .id(resultSet.getInt("country_id"))
                .name(resultSet.getString("country_name"))
                .build();
/*        User firstUser = User.builder()
                .id(resultSet.getInt("user_id"))
                .nickname(resultSet.getString("user_nickname"))
                .build();*/

        Review firstReview = Review.builder()
                .id(resultSet.getInt("review_id"))
                .userId(resultSet.getInt("user_id"))
                .text(resultSet.getString("review_text"))
                .build();

        List<Genre> genreList = new ArrayList<>();
        List<Country> countryList = new ArrayList<>();
        List<Review> reviewList = new ArrayList<>();
        genreList.add(firstGenre);
        countryList.add(firstCountry);
        reviewList.add(firstReview);

        while (resultSet.next()) {
            Genre genre = Genre.builder()
                    .id(resultSet.getInt("genre_id"))
                    .name(resultSet.getString("genre_name"))
                    .build();
            Country country = Country.builder()
                    .id(resultSet.getInt("country_id"))
                    .name(resultSet.getString("country_name"))
                    .build();
/*            User user = User.builder()
                    .id(resultSet.getInt("user_id"))
                    .nickname(resultSet.getString("user_nickname"))
                    .build();*/

            Review review = Review.builder()
                    .id(resultSet.getInt("review_id"))
                    .userId(resultSet.getInt("user_id"))
                    .text(resultSet.getString("review_text"))
                    .build();

            if (!genreList.contains(genre)) {
                genreList.add(genre);
            }
            if (!countryList.contains(country)) {
                countryList.add(country);
            }
            if (!reviewList.contains(review)) {
                reviewList.add(review);
            }
        }

        movieWithDetails.setGenres(genreList);
        movieWithDetails.setCountries(countryList);
        movieWithDetails.setReviews(reviewList);
        return movieWithDetails;
    }
}
