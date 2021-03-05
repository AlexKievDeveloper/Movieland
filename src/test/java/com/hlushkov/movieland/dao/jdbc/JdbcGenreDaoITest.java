package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
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
class JdbcGenreDaoITest {
    @Autowired
    private JdbcGenreDao jdbcGenreDao;

    @Test
    @DataSet(provider = TestData.GenreProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all genres from DB")
    void getAllGenres() {
        //when
        List<Genre> actualGenreList = jdbcGenreDao.findAll();
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

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of genres by movie id")
    void getByMovieId() {
        //when
        List<Genre> actualGenreList = jdbcGenreDao.findByMovieId(1);
        //then
        assertEquals(2, actualGenreList.size());
        assertEquals(1, actualGenreList.get(0).getId());
        assertEquals("драма", actualGenreList.get(0).getName());
        assertEquals(2, actualGenreList.get(1).getId());
        assertEquals("криминал", actualGenreList.get(1).getName());
    }
}