package com.hlushkov.movieland.dao.jdbc;

import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.hlushkov.movieland.entity.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcCountryDao implements CountryDao {
    private final CountryRowMapper countryRowMapper = new CountryRowMapper();
    private final JdbcTemplate jdbcTemplate;
    private final String findAllCountries;
    private final String findCountriesByMovieId;

    @Override
    public List<Country> findAll() {
        return jdbcTemplate.query(findAllCountries, countryRowMapper);
    }

    @Override
    public List<Country> findCountriesByMovieId(int movieId) {
        return jdbcTemplate.query(findCountriesByMovieId, countryRowMapper, movieId);
    }
}
