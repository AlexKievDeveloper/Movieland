package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestWebContextConfiguration
class DefaultMovieServiceTest {
    @Autowired
    private DefaultMovieService movieService;

    @Test
    void getPriceRoundedToTwoDigits() {
        //when
        double actualRoundedPrice = movieService.getPriceRoundedToTwoDigits(8.87552);
        //then
        assertEquals(8.88, actualRoundedPrice);
    }

}
