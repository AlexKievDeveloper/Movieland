package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.exception.NoUserFoundException;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcUserDaoITest {
    @Autowired
    private JdbcUserDao jdbcUserDao;

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns user by email")
    void findByEmail() {
        //when
        User actualUser = jdbcUserDao.findByEmail("ronald.reynolds66@example.com");
        //then
        assertEquals(1, actualUser.getId());
        assertEquals("Рональд Рейнольдс", actualUser.getNickname());
        assertEquals("ronald.reynolds66@example.com", actualUser.getEmail());
        assertEquals("paco", actualUser.getPassword());
        assertEquals("salt1", actualUser.getSalt());
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns user by email")
    void findByEmailIfUserNotExist() {
        //when+then
        assertThrows(NoUserFoundException.class, () -> {
            jdbcUserDao.findByEmail("test@example.com");
        });
    }
}