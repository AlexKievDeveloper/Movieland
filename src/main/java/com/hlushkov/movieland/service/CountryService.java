package com.hlushkov.movieland.service;

import com.hlushkov.movieland.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> findCountriesByMovieId(int movieId);
}
