package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.entity.Country;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CachedCountryDaoITest {
    @Autowired
    private CachedCountryDao cachedCountryDao;

    @Test
    @DisplayName("Returns list of countries from cachedCountryList")
    void findAll() {
        //when
        List<Country> actualCountryList = cachedCountryDao.findAll();

        //then
        assertNotNull(actualCountryList);
        assertEquals(7, actualCountryList.size());
    }

}