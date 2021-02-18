package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcUserDaoITest {
    @Autowired
    private JdbcUserDao jdbcUserDao;

    @Test
    @DataSet(provider = TestConfiguration.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns user by email")
    void findByEmail() {
        //when
        User actualUser = jdbcUserDao.findByEmail("ronald.reynolds66@example.com").get();
        //then
        assertEquals(1, actualUser.getId());
        assertEquals("Рональд Рейнольдс", actualUser.getNickname());
        assertEquals("ronald.reynolds66@example.com", actualUser.getEmail());
        assertEquals("paco", actualUser.getPassword());
        assertEquals("salt1", actualUser.getSalt());
    }
}