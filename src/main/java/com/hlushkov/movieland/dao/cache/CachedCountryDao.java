package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.entity.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
public class CachedCountryDao implements CountryDao {
    private final CountryDao countryDao;
    private volatile List<Country> cachedCountryList;

    @Override
    public List<Country> findAll() {
        return new ArrayList<>(cachedCountryList);
    }

    @Override
    public List<Country> findCountriesByMovieId(int movieId) {
        return countryDao.findCountriesByMovieId(movieId);
    }

    @PostConstruct
    @Scheduled(initialDelayString = "${country.cache.update.time.interval}", fixedRateString = "${country.cache.update.time.interval}")
    private void updateCacheValues() {
        log.info("Refresh country cache");
        cachedCountryList = countryDao.findAll();
    }

}
