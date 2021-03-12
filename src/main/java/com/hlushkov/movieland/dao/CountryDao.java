package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.Country;

import java.util.List;

public interface CountryDao {

    void saveMoviesCountries(List<Integer> countriesIds, int movieId);

    List<Country> findCountriesByMovieId(int movieId);
}
