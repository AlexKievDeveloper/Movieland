package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Review;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewRowMapperTest {
    @Mock
    private ResultSet resultSet;
    private ReviewRowMapper rowMapper;

    public ReviewRowMapperTest() {
        this.rowMapper = new ReviewRowMapper();
    }

    @Test
    @DisplayName("Returns review from result set")
    void mapRow() throws SQLException {
        //prepare
        when(resultSet.getInt("review_id")).thenReturn(1);
        when(resultSet.getInt("movie_id")).thenReturn(1);
        when(resultSet.getString("review_text")).thenReturn("Nice film!");
        when(resultSet.getInt("user_id")).thenReturn(1);

        //when
        Review actualReview = rowMapper.mapRow(resultSet, 0);

        //then
        assertEquals(1, actualReview.getId());
        assertEquals(1, actualReview.getMovieId());
        assertEquals(1, actualReview.getUserId());
        assertEquals("Nice film!", actualReview.getText());
    }
}