package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CachedGenreDaoTest {
    @Autowired
    private CachedGenreDao cachedDao;

    @Test
    @DisplayName("Returns list of genres from cachedGenreList")
    void getAllGenres() {
        //when
        List<Genre> actualGenreList = cachedDao.findAll();

        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());
    }
}



