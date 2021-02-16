package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieRowMapperTest {
    private final MovieRowMapper rowMapper;
    @Mock
    private ResultSet resultSet;

    public MovieRowMapperTest() {
        this.rowMapper = new MovieRowMapper();
    }

    @Test
    @DisplayName("Map row from result set")
    void mapRow() throws SQLException {
        //prepare
        when(resultSet.getInt("movie_id")).thenReturn(1);
        when(resultSet.getString("movie_name_russian")).thenReturn("Побег из Шоушенка");
        when(resultSet.getString("movie_name_native")).thenReturn("The Shawshank Redemption");
        when(resultSet.getInt("movie_year_of_release")).thenReturn(1994);
        when(resultSet.getString("movie_description")).thenReturn("Amazing film");
        when(resultSet.getDouble("movie_rating")).thenReturn(8.9);
        when(resultSet.getDouble("movie_price")).thenReturn(123.45);
        when(resultSet.getString("poster_picture_path")).thenReturn("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        //when
        Movie actualMovie = rowMapper.mapRow(resultSet, 0);
        //then
        assertEquals(1, actualMovie.getId());
        assertEquals("Побег из Шоушенка", actualMovie.getNameRussian());
        assertEquals("The Shawshank Redemption", actualMovie.getNameNative());
        assertEquals(1994, actualMovie.getYearOfRelease());
        assertEquals("Amazing film", actualMovie.getDescription());
        assertEquals(8.9, actualMovie.getRating());
        assertEquals(123.45, actualMovie.getPrice());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", actualMovie.getPicturePath());
    }
}
