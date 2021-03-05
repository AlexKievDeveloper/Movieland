package com.hlushkov.movieland.dao.jdbc.mapper;

import com.hlushkov.movieland.entity.Country;
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
class CountryRowMapperTest {
    @Mock
    private ResultSet resultSet;
    private CountryRowMapper countryRowMapper;

    public CountryRowMapperTest() {
        this.countryRowMapper = new CountryRowMapper();
    }

    @Test
    @DisplayName("Returns Country from result set")
    void mapRow() throws SQLException {
        //prepare
        when(resultSet.getInt("country_id")).thenReturn(1);
        when(resultSet.getString("country_name")).thenReturn("США");
        //when
        Country actualCountry = countryRowMapper.mapRow(resultSet, 0);
        //then
        assertEquals(1, actualCountry.getId());
        assertEquals("США", actualCountry.getName());
    }
}