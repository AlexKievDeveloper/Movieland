package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.common.dto.MovieDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDetailsRowMapper implements RowMapper<MovieDetails> {
    @Override
    public MovieDetails mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return MovieDetails.builder()
                .id(resultSet.getInt("movie_id"))
                .nameRussian(resultSet.getString("movie_name_russian"))
                .nameNative(resultSet.getString("movie_name_native"))
                .description(resultSet.getString("movie_description"))
                .yearOfRelease(resultSet.getInt("movie_year_of_release"))
                .rating(resultSet.getDouble("movie_rating"))
                .price(resultSet.getDouble("movie_price"))
                .picturePath(resultSet.getString("movie_picture_path"))
                .build();
    }
}
