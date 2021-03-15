package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.Role;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import com.hlushkov.movieland.entity.User;
import com.hlushkov.movieland.security.util.UserHolder;
import com.hlushkov.movieland.service.impl.DefaultCurrencyService;
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
class MovieControllerITest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private DefaultCurrencyService currencyService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setMockMvc() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class,
            executeStatementsBefore = "SELECT setval('movies_movie_id_seq', 25)", cleanAfter = true)
    @ExpectedDataSet(provider = TestData.AddMovieResultProvider.class)
    @DisplayName("Saves movie to db")
    void saveMovie() throws Exception {
        //prepare
        List<Integer> countriesList = List.of(1, 2);
        List<Integer> genresList = List.of(1, 2, 3);
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("nameRussian", "Побег из тюрьмы Шоушенка");
        parametersMap.put("nameNative", "The Shawshank Redemption prison");
        parametersMap.put("yearOfRelease", 1994);
        parametersMap.put("description", "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.");
        parametersMap.put("price", 123.45);
        parametersMap.put("rating", 8.0);
        parametersMap.put("picturePath", "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        parametersMap.put("countriesIds", countriesList);
        parametersMap.put("genresIds", genresList);
        String addMovieJson = objectMapper.writeValueAsString(parametersMap);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(post("/movies")
                    .content(addMovieJson)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of all movies in json format")
    void findAllMovies() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies"))
                    .andDo(print())
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
            assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
            assertTrue(response.getContentAsString().contains("Dances with Wolves"));
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of all movies sorted by rating DESC in json format")
    void findAllMoviesSortedByRatingDESC() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies").param("rating", "desc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].rating").value("8.9"))
                    .andExpect(jsonPath("$[24].rating").value("7.6"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
            assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
            assertTrue(response.getContentAsString().contains("Dances with Wolves"));
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of all movies sorted by price DESC in json format")
    void findAllMoviesSortedByPriceDESC() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies").param("price", "desc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].price").value("200.6"))
                    .andExpect(jsonPath("$[24].price").value("100.0"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
            assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
            assertTrue(response.getContentAsString().contains("Dances with Wolves"));
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of all movies sorted by price ASC in json format")
    void findAllMoviesSortedByPriceASC() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies").param("price", "asc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].price").value("100.0"))
                    .andExpect(jsonPath("$[24].price").value("200.6"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
            assertTrue(response.getContentAsString().contains("The Shawshank Redemption"));
            assertTrue(response.getContentAsString().contains("Dances with Wolves"));
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of three random movies in json format")
    void findThreeRandomMovies() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/random"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].id").isNotEmpty())
                    .andExpect(jsonPath("$[0].nameRussian").isNotEmpty())
                    .andExpect(jsonPath("$[0].nameNative").isNotEmpty())
                    .andExpect(jsonPath("$[0].yearOfRelease").isNotEmpty())
                    .andExpect(jsonPath("$[0].description").isNotEmpty())
                    .andExpect(jsonPath("$[0].rating").isNotEmpty())
                    .andExpect(jsonPath("$[0].price").isNotEmpty())
                    .andExpect(jsonPath("$[0].picturePath").isNotEmpty())

                    .andExpect(jsonPath("$[1].id").isNotEmpty())
                    .andExpect(jsonPath("$[1].nameRussian").isNotEmpty())
                    .andExpect(jsonPath("$[1].nameNative").isNotEmpty())
                    .andExpect(jsonPath("$[1].yearOfRelease").isNotEmpty())
                    .andExpect(jsonPath("$[1].description").isNotEmpty())
                    .andExpect(jsonPath("$[1].rating").isNotEmpty())
                    .andExpect(jsonPath("$[1].price").isNotEmpty())
                    .andExpect(jsonPath("$[1].picturePath").isNotEmpty())

                    .andExpect(jsonPath("$[2].id").isNotEmpty())
                    .andExpect(jsonPath("$[2].nameRussian").isNotEmpty())
                    .andExpect(jsonPath("$[2].nameNative").isNotEmpty())
                    .andExpect(jsonPath("$[2].yearOfRelease").isNotEmpty())
                    .andExpect(jsonPath("$[2].description").isNotEmpty())
                    .andExpect(jsonPath("$[2].rating").isNotEmpty())
                    .andExpect(jsonPath("$[2].price").isNotEmpty())
                    .andExpect(jsonPath("$[2].picturePath").isNotEmpty())

                    .andExpect(jsonPath("$[3].id").doesNotExist())
                    .andExpect(jsonPath("$[3].nameRussian").doesNotExist())
                    .andExpect(jsonPath("$[3].nameNative").doesNotExist())
                    .andExpect(jsonPath("$[3].yearOfRelease").doesNotExist())
                    .andExpect(jsonPath("$[3].description").doesNotExist())
                    .andExpect(jsonPath("$[3].rating").doesNotExist())
                    .andExpect(jsonPath("$[3].price").doesNotExist())
                    .andExpect(jsonPath("$[3].picturePath").doesNotExist())

                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre in json format")
    void findAllMoviesByGenre() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/genre/15"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].id").isNotEmpty())
                    .andExpect(jsonPath("$[1].id").isNotEmpty())
                    .andExpect(jsonPath("$[2].id").isNotEmpty())
                    .andExpect(jsonPath("$[3].id").doesNotExist())
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by rating in json format")
    void findAllMoviesByGenreSortedByRating() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/genre/15?rating=desc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].rating").value("8.5"))
                    .andExpect(jsonPath("$[1].rating").value("8.5"))
                    .andExpect(jsonPath("$[2].rating").value("8.0"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by price DESC in json format")
    void findAllMoviesByGenreSortedByPriceDesc() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/genre/15?price=desc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].price").value("170.0"))
                    .andExpect(jsonPath("$[1].price").value("130.0"))
                    .andExpect(jsonPath("$[2].price").value("120.55"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by price ASC in json format")
    void findAllMoviesByGenreSortedByPriceAsc() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/genre/15?price=asc"))
                    .andDo(print())
                    .andExpect(jsonPath("$[0].price").value("120.55"))
                    .andExpect(jsonPath("$[1].price").value("130.0"))
                    .andExpect(jsonPath("$[2].price").value("170.0"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns movie by id in json format")
    void findById() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/1"))
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.nameNative").value("The Shawshank Redemption"))
                    .andExpect(jsonPath("$.yearOfRelease").value("1994"))
                    .andExpect(jsonPath("$.rating").value("8.9"))
                    .andExpect(jsonPath("$.price").value("123.45"))
                    .andExpect(jsonPath("$.countries.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[0].id").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].userId").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[1].userId").value("3"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanBefore = true, cleanAfter = true)
    @DisplayName("Returns movie by id in json format value in USD")
    void findByIdWithCurrencyUSD() throws Exception {
        //prepare
        double expectedPrice = 123.45 / currencyService.getCurrencyExchangeRate("USD");
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/1")
                    .param("currency", "USD"))
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.nameNative").value("The Shawshank Redemption"))
                    .andExpect(jsonPath("$.yearOfRelease").value("1994"))
                    .andExpect(jsonPath("$.rating").value("8.9"))
                    .andExpect(jsonPath("$.price").value(expectedPrice))
                    .andExpect(jsonPath("$.countries.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[0].id").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].userId").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[1].userId").value("3"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns movie by id in json format value in EUR")
    void findByIdWithCurrencyEUR() throws Exception {
        //prepare
        double expectedPrice = 123.45 / currencyService.getCurrencyExchangeRate("EUR");
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.USER).build());

            MockHttpServletResponse response = mockMvc.perform(get("/movies/1")
                    .param("currency", "EUR"))
                    .andDo(print())
                    .andExpect(jsonPath("$.id").value("1"))
                    .andExpect(jsonPath("$.nameNative").value("The Shawshank Redemption"))
                    .andExpect(jsonPath("$.yearOfRelease").value("1994"))
                    .andExpect(jsonPath("$.rating").value("8.9"))
                    .andExpect(jsonPath("$.price").value(expectedPrice))
                    .andExpect(jsonPath("$.countries.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[0].id").value("1"))
                    .andExpect(jsonPath("$.genres.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[0].id").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[0].userId").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].id").value("2"))
                    .andExpect(jsonPath("$.reviews.[1].movieId").value("1"))
                    .andExpect(jsonPath("$.reviews.[1].userId").value("3"))
                    .andExpect(status().isOk()).andReturn().getResponse();
            //then
            assertNotNull(response.getHeader("Content-Type"));
            assertEquals("application/json", response.getHeader("Content-Type"));
            assertEquals("application/json", response.getContentType());
            assertNotNull(response.getContentAsString());
        }
    }

    @Test
    @DataSet(provider = TestData.EditMovieDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMovieDataResultProvider.class)
    @DisplayName("Updates movie with genres and countries by movie id")
    void editMovie() throws Exception {
        //prepare
        Map<String, Object> parametersMap = new HashMap();
        parametersMap.put("nameRussian", "Побег из тюрьмы Шоушенка");
        parametersMap.put("nameNative", "The Shawshank Redemption prison");
        parametersMap.put("yearOfRelease", 1994);
        parametersMap.put("description", "Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает разрабатывать невероятно дерзкий план своего освобождения.");
        parametersMap.put("price", 123.45);
        parametersMap.put("rating", 8.0);
        parametersMap.put("picturePath", "https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg");
        String addMovieJson = objectMapper.writeValueAsString(parametersMap);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(put("/movies/2")
                    .content(addMovieJson)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DataSet(provider = TestData.AddMoviesGenresDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMoviesGenresResultProvider.class)
    @DisplayName("Edit movie genres")
    void editMovieGenres() throws Exception {
        //prepare
        List<Integer> genresIds = List.of(3, 4);
        String editMovieGenresJson = objectMapper.writeValueAsString(genresIds);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(put("/movies/1/genres")
                    .content(editMovieGenresJson)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DataSet(provider = TestData.AddMoviesCountriesDataProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.EditMoviesCountriesResultProvider.class)
    @DisplayName("Edit movie countries")
    void editMovieCountries() throws Exception {
        //prepare
        List<Integer> genresIds = List.of(3, 4);
        String editMovieCountriesJson = objectMapper.writeValueAsString(genresIds);

        //when+then
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(put("/movies/1/countries")
                    .content(editMovieCountriesJson)
                    .contentType("application/json"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DataSet(provider = TestData.ReviewsProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveReviewsResultProvider.class)
    @DisplayName("Remove reviews by movie id")
    void removeReviewsByMovieId() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(delete("/movies/1/reviews"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Test
    @DataSet(provider = TestData.ReviewsProvider.class, cleanAfter = true)
    @ExpectedDataSet(provider = TestData.RemoveReviewsResultProvider.class)
    @DisplayName("Returns 404 if data doest not exist")
    void removeReviewsByMovieIdIfMovieDoesNotExist() throws Exception {
        //when
        try (MockedStatic<UserHolder> theMock = Mockito.mockStatic(UserHolder.class)) {
            theMock.when(UserHolder::getUser).thenReturn(User.builder().role(Role.ADMIN).build());

            mockMvc.perform(delete("/movies/100/reviews"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

}