package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.dao.CountryDao;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcCountryDaoITest {
    @Autowired
    private CountryDao countryDao;

    @Test
    void findAll() {
        //when
        List<Country> actualCountries = countryDao.findAll();
        //then
        assertNotNull(actualCountries);
        assertEquals(7, actualCountries.size());
    }

    @Test
    @DataSet(provider = TestData.AddMovieResultProvider.class, cleanAfter = true)
    void findCountriesByMovieId() {
        //when
        List<Country> actualCountries = countryDao.findCountriesByMovieId(1);
        //then
        assertNotNull(actualCountries);
        assertEquals(1, actualCountries.size());
        assertEquals("США", actualCountries.get(0).getName());
    }
}