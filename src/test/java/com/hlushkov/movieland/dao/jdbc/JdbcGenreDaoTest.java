package com.hlushkov.movieland.dao.jdbc;

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
class JdbcGenreDaoTest {
    @Autowired
    private JdbcGenreDao jdbcGenreDao;

    @Test
    @DisplayName("Returns list with all genres from DB")
    void getAllGenres() {
        //when
        List<Genre> actualGenreList = jdbcGenreDao.findAllGenres();
        //then
        assertNotNull(actualGenreList);
        assertEquals(15, actualGenreList.size());

        for (int i = 0; i < actualGenreList.size(); i++) {
            assertEquals(i + 1, actualGenreList.get(i).getId());
        }

        assertEquals("драма", actualGenreList.get(0).getName());
        assertEquals(1, actualGenreList.get(0).getId());

        assertEquals("вестерн", actualGenreList.get(14).getName());
        assertEquals(15, actualGenreList.get(14).getId());
    }
}