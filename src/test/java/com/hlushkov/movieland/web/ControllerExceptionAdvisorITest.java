package com.hlushkov.movieland.web;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.security.util.UserHolder;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@ExtendWith(MockitoExtension.class)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControllerExceptionAdvisorITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setMockMvc() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Catch NoUserFoundException and send message and status bad request as response")
    void badRequestNoUserFoundException() throws Exception {
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("email", "nouser@gmail.com");
        userInfo.put("password", "user");
        String jsonNoValidUserCredentials = objectMapper.writeValueAsString(userInfo);

        MockHttpServletResponse response = mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(jsonNoValidUserCredentials))
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Invalid email or password. Please check your credentials and try again."))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertNull(response.getCookie("user_uuid"));
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("application/json", response.getContentType());
        assertNotNull(response.getContentAsString());
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Catch IllegalArgumentException and send message and bad request status as response")
    void badRequestIllegalArgumentException() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            mockMvc.perform(get("/movie/26"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 25)", cleanAfter = true)
    @DisplayName("Catch DataAccessException and send message and bad request status as response")
    void badRequestDataAccessException() throws Exception {
        //prepare
        List<Integer> countriesList = List.of(1, 2);
        List<Integer> genresList = List.of(1, 2, 3);
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("nameRussian", "Побег из Шоушенка");
        parametersMap.put("nameNative", "The Shawshank Redemption prison");
        parametersMap.put("yearOfRelease", 1994);
        parametersMap.put("description", "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.");
        parametersMap.put("price", 123.45);
        parametersMap.put("rating", 8.0);
        parametersMap.put("picturePath", "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        parametersMap.put("countriesIds", countriesList);
        parametersMap.put("genresIds", genresList);
        String addMovieJson = objectMapper.writeValueAsString(parametersMap);

        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(put("/movie")
                    .contentType("application/json")
                    .content(addMovieJson))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}