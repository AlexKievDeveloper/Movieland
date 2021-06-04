package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Review;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewRowMapper implements RowMapper<Review> {
    @Override
    public Review mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Review.builder()
                .id(resultSet.getInt("review_id"))
                .movieId(resultSet.getInt("movie_id"))
                .text(resultSet.getString("review_text"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}
