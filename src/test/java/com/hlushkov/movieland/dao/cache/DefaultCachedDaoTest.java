package com.hlushkov.movieland.dao.cache;

import com.hlushkov.movieland.RootApplicationContext;
import com.hlushkov.movieland.TestConfiguration;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitWebConfig(value = {RootApplicationContext.class, TestConfiguration.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultCachedDaoTest {
    @Autowired
    private DefaultCachedDao cachedGenreDao;

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


