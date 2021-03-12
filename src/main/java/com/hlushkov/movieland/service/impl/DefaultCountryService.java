package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.entity.Country;
import com.hlushkov.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultCountryService implements CountryService {

    private final CountryDao countryDao;

    @Override
    public List<Country> findCountriesByMovieId(int movieId) {
        return countryDao.findCountriesByMovieId(movieId);
    }
}
