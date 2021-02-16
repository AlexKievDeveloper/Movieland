package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.dto.MovieWithDetails;
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
class MovieWithDetailsResultSetExtractorTest {
    @Mock
    private ResultSet resultSet;

    private MovieWithDetailsResultSetExtractor rowMapper;

    public MovieWithDetailsResultSetExtractorTest() {
        rowMapper = new MovieWithDetailsResultSetExtractor();
    }

    @Test
    @DisplayName("Returns MovieWithDetails from result set")
    void extractData() throws SQLException {
        //prepare
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("movie_id")).thenReturn(1);
        when(resultSet.getString("movie_name_russian")).thenReturn("Побег из Шоушенка");
        when(resultSet.getString("movie_name_native")).thenReturn("The Shawshank Redemption");
        when(resultSet.getString("movie_description")).thenReturn("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника");
        when(resultSet.getInt("movie_year_of_release")).thenReturn(1994);
        when(resultSet.getDouble("movie_rating")).thenReturn(8.9);
        when(resultSet.getDouble("movie_price")).thenReturn(123.45);
        when(resultSet.getString("poster_picture_path")).thenReturn("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");

        when(resultSet.getInt("genre_id")).thenReturn(1);
        when(resultSet.getString("genre_name")).thenReturn("драма");

        when(resultSet.getInt("country_id")).thenReturn(1);
        when(resultSet.getString("country_name")).thenReturn("США");

        when(resultSet.getInt("user_id")).thenReturn(2);
        when(resultSet.getString("user_name")).thenReturn("Дарлин Эдвардс");

        when(resultSet.getInt("review_id")).thenReturn(1);
        when(resultSet.getString("review_review")).thenReturn("Гениальное кино! Смотришь и думаешь «Так не бывает!»");
        //when
        MovieWithDetails actualMovieWithDetails = rowMapper.extractData(resultSet);
        //then
        assertEquals(1, actualMovieWithDetails.getId());
        assertEquals("Побег из Шоушенка", actualMovieWithDetails.getNameRussian());
        assertEquals("The Shawshank Redemption", actualMovieWithDetails.getNameNative());
        assertEquals("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника", actualMovieWithDetails.getDescription());
        assertEquals(1994, actualMovieWithDetails.getYearOfRelease());
        assertEquals(8.9, actualMovieWithDetails.getRating());
        assertEquals(123.45, actualMovieWithDetails.getPrice());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", actualMovieWithDetails.getPicturePath());

        assertEquals(1, actualMovieWithDetails.getGenres().get(0).getId());
        assertEquals("драма", actualMovieWithDetails.getGenres().get(0).getName());

        assertEquals(1, actualMovieWithDetails.getCountries().get(0).getId());
        assertEquals("США", actualMovieWithDetails.getCountries().get(0).getName());

        assertEquals(2, actualMovieWithDetails.getReviews().get(0).getUser().getId());
        assertEquals("Дарлин Эдвардс", actualMovieWithDetails.getReviews().get(0).getUser().getNickname());

        assertEquals(1, actualMovieWithDetails.getReviews().get(0).getId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!»", actualMovieWithDetails.getReviews().get(0).getText());
    }
}