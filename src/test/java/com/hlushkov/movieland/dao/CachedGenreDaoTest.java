package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.RootApplicationContext;
import com.hlushkov.movieland.TestConfiguration;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitWebConfig(value = {TestConfiguration.class, RootApplicationContext.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class) //TODO ПОЧЕМУ ТЕСТ НЕ ПРОХОДИТ БЕЗ АННОТАЦИИ МОКИТО
class CachedGenreDaoTest {
    @Autowired
    private CachedGenreDao cachedGenreDao;

    @Test
    @DisplayName("Returns list of genres from cachedGenreList")
    void getAllGenres() {
        //when
        List<Genre> actualGenreList = cachedGenreDao.findAllGenres();

        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());
    }
}



