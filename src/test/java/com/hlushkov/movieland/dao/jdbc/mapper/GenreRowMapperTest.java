package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Genre;
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
class GenreRowMapperTest {
    private final GenreRowMapper rowMapper;
    @Mock
    private ResultSet resultSet;

    public GenreRowMapperTest() {
        this.rowMapper = new GenreRowMapper();
    }

    @Test
    @DisplayName("Map row from result set")
    void mapRow() throws SQLException {
        //prepare
        when(resultSet.getString("name")).thenReturn("драма");
        when(resultSet.getInt("id")).thenReturn(1);
        //when
        Genre actualGenre = rowMapper.mapRow(resultSet, 0);
        //then
        assertEquals(1, actualGenre.getId());
        assertEquals("драма", actualGenre.getName());
    }
}