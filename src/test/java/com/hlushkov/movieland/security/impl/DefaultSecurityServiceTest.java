package com.hlushkov.movieland.security.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultSecurityServiceTest {
    @Autowired
    private SecurityService securityService;

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns optional with Session")
    void login() {
        //when
        Optional<Session> actualOptionalSession = securityService.login("user@gmail.com", "user");
        //then
        assertTrue(actualOptionalSession.isPresent());
        assertEquals("user", actualOptionalSession.get().getUser().getNickname());
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns empty optional")
    void loginWithIncorrectPassword() {
        //when
        Optional<Session> actualOptionalSession = securityService.login("user@gmail.com", "noValidUserPassword");
        //then
        assertTrue(actualOptionalSession.isEmpty());
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns empty optional")
    void loginWithIncorrectEmail() {
        //when
        Optional<Session> actualOptionalSession = securityService.login("noValidUserEmail@gmail.com", "user");
        //then
        assertTrue(actualOptionalSession.isEmpty());
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns true")
    void removeSession() {
        //prepare
        Optional<Session> optionalSession = securityService.login("user@gmail.com", "user");
        //when
        boolean doesSessionRemoved = securityService.removeSession(optionalSession.get().getUserUUID());
        //then
        assertTrue(doesSessionRemoved);
    }

    @Test
    @DataSet(provider = TestConfiguration.UserProvider.class)
    @DisplayName("Returns optional with session")
    void getSession() {
        //prepare
        Optional<Session> optionalSession = securityService.login("user@gmail.com", "user");
        //when
        Optional<Session> actualOptionalSession = securityService.getSession(optionalSession.get().getUserUUID());
        //then
        assertFalse(actualOptionalSession.isEmpty());
        assertEquals(actualOptionalSession.get().getUserUUID(), optionalSession.get().getUserUUID());
        assertEquals(1, actualOptionalSession.get().getUser().getId());
        assertEquals("user", actualOptionalSession.get().getUser().getNickname());
        assertEquals("user@gmail.com", actualOptionalSession.get().getUser().getEmail());
        assertEquals("e172c5654dbc12d78ce1850a4f7956ba6e5a3d2ac40f0925fc6d691ebb54f6bf", actualOptionalSession.get().getUser().getPassword());
        assertEquals("user", actualOptionalSession.get().getUser().getSalt());
        assertEquals(Role.USER, actualOptionalSession.get().getUser().getRole());
    }

}