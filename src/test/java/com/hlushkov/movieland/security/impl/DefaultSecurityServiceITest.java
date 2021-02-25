package com.hlushkov.movieland.security.impl;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.common.exception.NoUserFoundException;
import com.hlushkov.movieland.common.request.AuthRequest;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.SecurityService;
import com.hlushkov.movieland.security.session.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultSecurityServiceITest {
    @Autowired
    private SecurityService securityService;

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns optional with Session")
    void login() {
        //prepare
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@gmail.com");
        authRequest.setPassword("user");

        //when
        Optional<Session> actualOptionalSession = securityService.login(authRequest);
        //then
        assertTrue(actualOptionalSession.isPresent());
        assertEquals("user", actualOptionalSession.get().getUser().getNickname());
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns empty optional")
    void loginWithIncorrectPassword() {
        //prepare
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@gmail.com");
        authRequest.setPassword("noValidUserPassword");

        //when
        Optional<Session> actualOptionalSession = securityService.login(authRequest);
        //then
        assertTrue(actualOptionalSession.isEmpty());
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns empty optional")
    void loginWithIncorrectEmail() {
        //prepare
        AuthRequest authRequestWithNoValidCredentials = new AuthRequest();
        authRequestWithNoValidCredentials.setEmail("noValidUserEmail@gmail.com");
        authRequestWithNoValidCredentials.setPassword("user");

        //when
        NoUserFoundException thrown = Assertions.assertThrows(
                NoUserFoundException.class,
                () -> securityService.login(authRequestWithNoValidCredentials));

        assertEquals("Exception while getting user by email from db: noValidUserEmail@gmail.com", thrown.getMessage());
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns true")
    void removeSession() {
        //prepare
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@gmail.com");
        authRequest.setPassword("user");

        Optional<Session> optionalSession = securityService.login(authRequest);
        //when
        boolean doesSessionRemoved = securityService.removeSession(optionalSession.get().getUserUUID());
        //then
        assertTrue(doesSessionRemoved);
    }

    @Test
    @DataSet(provider = TestData.UserProvider.class, cleanAfter = true)
    @DisplayName("Returns optional with session")
    void getSession() {
        //prepare
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@gmail.com");
        authRequest.setPassword("user");

        Optional<Session> optionalSession = securityService.login(authRequest);
        //when
        Optional<User> actualOptionalUser = securityService.getUserByUUID(optionalSession.get().getUserUUID());
        //then
        assertFalse(actualOptionalUser.isEmpty());
        assertEquals(1, actualOptionalUser.get().getId());
        assertEquals("user", actualOptionalUser.get().getNickname());
        assertEquals("user@gmail.com", actualOptionalUser.get().getEmail());
        assertEquals("e172c5654dbc12d78ce1850a4f7956ba6e5a3d2ac40f0925fc6d691ebb54f6bf", actualOptionalUser.get().getPassword());
        assertEquals("user", actualOptionalUser.get().getSalt());
        assertEquals(Role.USER, actualOptionalUser.get().getRole());
    }

}