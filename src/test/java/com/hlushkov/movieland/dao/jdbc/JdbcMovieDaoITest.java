package com.hlushkov.movieland.dao.jdbc;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.spring.api.DBRider;
import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.config.TestConfiguration;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.dto.MovieWithDetails;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.request.MovieRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
@TestWebContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMovieDaoITest {
    @Autowired
    private JdbcMovieDao jdbcMovieDao;

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class)
    @DisplayName("Returns list with all movies from DB")
    void getAllMovies() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorting by rating DESC")
    void getAllMoviesWithRatingDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class)
    @DisplayName("Returns list with all movies from DB sorted by price DESC")
    void getAllMoviesWithPriceDESCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MovieProvider.class, cleanAfter = true)
    @DisplayName("Returns list with all movies from DB sorted by price ASC")
    void getAllMoviesWithPriceASCDirectionTest() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.ofNullable(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findAll(movieRequest);

        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesProvider.class)
    @DisplayName("Returns list with all movies from DB")
    void getRandomMovies() {
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findRandom();
        //then
        assertNotNull(actualMovieList);
        assertEquals(3, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre from DB")
    void getMoviesByGenre() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by rating from DB")
    void getMoviesByGenreSortedByRating() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setRatingDirection(Optional.of(SortDirection.DESC));
        movieRequest.setPriceDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(8.9, actualMovieList.get(0).getRating());
        assertEquals(8.8, actualMovieList.get(1).getRating());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price DESC from DB")
    void getMoviesByGenreSortedByPriceDesc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.DESC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(134.67, actualMovieList.get(0).getPrice());
        assertEquals(123.45, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesByGenresProvider.class, cleanAfter = true)
    @DisplayName("Returns list with movies by genre sorted by price ASC from DB")
    void getMoviesByGenreSortedByPriceAsc() {
        //prepare
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setPriceDirection(Optional.of(SortDirection.ASC));
        movieRequest.setRatingDirection(Optional.ofNullable(null));
        //when
        List<Movie> actualMovieList = jdbcMovieDao.findByGenre(2, movieRequest);
        //then
        assertNotNull(actualMovieList);
        assertEquals(2, actualMovieList.size());
        assertEquals(123.45, actualMovieList.get(0).getPrice());
        assertEquals(134.67, actualMovieList.get(1).getPrice());
    }

    @Test
    @DataSet(provider = TestConfiguration.MoviesCountriesGenresReviewsUsers.class, cleanAfter = true)
    @DisplayName("Returns Movie with details by movie id")
    void findMovieWithDetailsByMovieId() {
        //when
        MovieWithDetails actualMovieWithDetails = jdbcMovieDao.findMovieWithDetailsByMovieId(1);
        //then
        assertEquals(1, actualMovieWithDetails.getId());
        assertEquals("Побег из Шоушенка", actualMovieWithDetails.getNameRussian());
        assertEquals("The Shawshank Redemption", actualMovieWithDetails.getNameNative());
        assertEquals("Успешный банкир Энди Дюфрейн обвинен в убийстве собственной жены и ее любовника. " +
                "Оказавшись в тюрьме под названием Шоушенк, он сталкивается с жестокостью и беззаконием, царящими по обе" +
                " стороны решетки. Каждый, кто попадает в эти стены, становится их рабом до конца жизни. Но Энди, " +
                "вооруженный живым умом и доброй душой, отказывается мириться с приговором судьбы и начинает " +
                "разрабатывать невероятно дерзкий план своего освобождения.", actualMovieWithDetails.getDescription());
        assertEquals(1994, actualMovieWithDetails.getYearOfRelease());
        assertEquals(8.9, actualMovieWithDetails.getRating());
        assertEquals(123.45, actualMovieWithDetails.getPrice());
        assertEquals("https://images-na.ssl-images-amazon.com/images/M/MV5BODU4MjU4NjIwNl5BMl5BanBnXkFtZTgwMDU2MjEyMDE@._V1._SY209_CR0,0,140,209_.jpg", actualMovieWithDetails.getPicturePath());

        assertEquals(1, actualMovieWithDetails.getGenres().get(0).getId());
        assertEquals("драма", actualMovieWithDetails.getGenres().get(0).getName());
        assertEquals(2, actualMovieWithDetails.getGenres().get(1).getId());
        assertEquals("криминал", actualMovieWithDetails.getGenres().get(1).getName());

        assertEquals(1, actualMovieWithDetails.getCountries().get(0).getId());
        assertEquals("США", actualMovieWithDetails.getCountries().get(0).getName());

        assertEquals(2, actualMovieWithDetails.getReviews().size());
        assertEquals(1, actualMovieWithDetails.getReviews().get(0).getId());
        assertEquals("Гениальное кино! Смотришь и думаешь «Так не бывает!», но позже понимаешь, то только так " +
                        "и должно быть. Начинаешь заново осмысливать значение фразы, которую постоянно используешь в своей жизни," +
                        " «Надежда умирает последней». Ведь если ты не надеешься, то все в твоей жизни гаснет, не остается смысла." +
                        " Фильм наполнен бесконечным числом правильных афоризмов. Я уверена, что буду пересматривать его сотни раз.",
                actualMovieWithDetails.getReviews().get(0).getText());
        assertEquals(2, actualMovieWithDetails.getReviews().get(1).getId());
        assertEquals("Кино это является, безусловно, «со знаком качества». Что же до первого места в рейтинге, " +
                        "то, думаю, здесь имело место быть выставление «десяточек» от большинства зрителей вкупе с " +
                        "раздутыми восторженными откликами кинокритиков. 'Фильм атмосферный. Он драматичный. И, конечно," +
                        " заслуживает того, чтобы находиться довольно высоко в мировом кинематографе.",
                actualMovieWithDetails.getReviews().get(1).getText());


        assertEquals(2, actualMovieWithDetails.getReviews().get(0).getUser().getId());
        assertEquals("Дарлин Эдвардс", actualMovieWithDetails.getReviews().get(0).getUser().getNickname());
        assertEquals(3, actualMovieWithDetails.getReviews().get(1).getUser().getId());
        assertEquals("Габриэль Джексон", actualMovieWithDetails.getReviews().get(1).getUser().getNickname());
    }

}