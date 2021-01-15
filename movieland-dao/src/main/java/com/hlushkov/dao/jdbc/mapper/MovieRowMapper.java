package com.hlushkov.dao.jdbc.mapper;

import entity.Movie;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieRowMapper implements RowMapper<Movie> {

    @Override
    public Movie mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Movie.builder()
                .id(resultSet.getInt("movie_id"))
                .nameRussian(resultSet.getString("nameRussian"))
                .nameNative(resultSet.getString("nameNative"))
                .yearOfRelease(resultSet.getInt("yearOfRelease"))
                .rating(resultSet.getDouble("rating"))
                .price(resultSet.getDouble("price"))
                .picturePath(resultSet.getString("picturePath"))
                .build();
    }
}
