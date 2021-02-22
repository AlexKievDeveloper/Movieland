package com.hlushkov.movieland.web.controller;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.data.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @BeforeEach
    void setMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(sharedHttpSession()).build();
    }

    @Test
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Returns list of all movies in json format")
    void findAllMovies() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie"))
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

    @Test
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Returns list of all movies sorted by rating DESC in json format")
    void findAllMoviesSortedByRatingDESC() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie").param("rating", "desc"))
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

    @Test
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Returns list of all movies sorted by price DESC in json format")
    void findAllMoviesSortedByPriceDESC() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie").param("price", "desc"))
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

    @Test
    @DataSet(provider = TestData.MoviesProvider.class)
    @DisplayName("Returns list of all movies sorted by price ASC in json format")
    void findAllMoviesSortedByPriceASC() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie").param("price", "asc"))
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

    @Test
    @DataSet(provider = TestData.PostersAndMoviesProvider.class, cleanAfter = true)
    @DisplayName("Returns list of three random movies in json format")
    void findThreeRandomMovies() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/random"))
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

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre in json format")
    void findAllMoviesByGenre() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/genre/15"))
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

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by rating in json format")
    void findAllMoviesByGenreSortedByRating() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/genre/15?rating=desc"))
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

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by price DESC in json format")
    void findAllMoviesByGenreSortedByPriceDesc() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/genre/15?price=desc"))
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

    @Test
    @DataSet(provider = TestData.MoviesGenresFullProvider.class, cleanAfter = true)
    @DisplayName("Returns list of movies by genre sorted by price ASC in json format")
    void findAllMoviesByGenreSortedByPriceAsc() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/genre/15?price=asc"))
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

    @Test
    @DataSet(provider = TestData.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns movie by id in json format")
    void findMovieById() throws Exception {
        //when
        MockHttpServletResponse response = mockMvc.perform(get("/movie/1"))
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
                .andExpect(jsonPath("$.reviews.[1].id").value("2"))
                .andExpect(jsonPath("$.reviews.[0].user.id").value("2"))
                .andExpect(jsonPath("$.reviews.[1].user.id").value("3"))
                .andExpect(status().isOk()).andReturn().getResponse();
        //then
        assertNotNull(response.getHeader("Content-Type"));
        assertEquals("application/json", response.getHeader("Content-Type"));
        assertEquals("application/json", response.getContentType());
        assertNotNull(response.getContentAsString());
    }
}