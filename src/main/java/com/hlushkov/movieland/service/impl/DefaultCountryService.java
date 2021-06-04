package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.entity.Country;
import com.hlushkov.movieland.service.CountryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DefaultCountryService implements CountryService {
    private final CountryDao countryDao;

    @Override
    public List<Country> findCountriesByMovieId(int movieId) {
        log.info("Method findCountriesByMovieId started: {}", LocalDateTime.now());
        return countryDao.findCountriesByMovieId(movieId);
    }
}
