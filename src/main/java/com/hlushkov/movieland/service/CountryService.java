package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Country;

import java.util.List;

public interface CountryService {

    void saveMoviesCountries(List<Integer> countriesIds, int movieId);

    List<Country> findCountriesByMovieId(int movieId);

}
