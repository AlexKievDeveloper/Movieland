package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.RootApplicationContext;
import com.hlushkov.movieland.TestConfiguration;
import com.hlushkov.movieland.dao.jdbc.JdbcGenreDao;
import com.hlushkov.movieland.entity.Genre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringJUnitWebConfig(value = {TestConfiguration.class, RootApplicationContext.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class CachedGenreDaoTest {
    @Mock
    private JdbcGenreDao jdbcGenreDao;
    @InjectMocks
    private CachedGenreDao cachedGenreDao;

    @Test
    @DisplayName("Returns list of genres. On the first invoke using jdbcGenreDao, on the second and third invoke from cachedGenreList")
    void getAllGenres() {
        //prepare
        List<Genre> genreList = List.of(new Genre(1, "детектив"), new Genre(2, "боевик"));
        when(jdbcGenreDao.getAllGenres()).thenReturn(genreList);

        //when
        cachedGenreDao.getAllGenres();
        cachedGenreDao.getAllGenres();
        cachedGenreDao.getAllGenres();

        //then
        verify(jdbcGenreDao).getAllGenres();
    }

}