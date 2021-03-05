package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Country;

import java.util.List;

public interface CountryDao {
    List<Country> findCountriesByMovieId(int movieId);
}
