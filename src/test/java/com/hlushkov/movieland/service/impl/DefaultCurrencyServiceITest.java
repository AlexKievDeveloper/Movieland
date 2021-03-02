package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@TestWebContextConfiguration
class DefaultCurrencyServiceITest {
    @Autowired
    private DefaultCurrencyService currencyService;

    @Test
    @DisplayName("Returns currency exchange rates")
    void getCurrencyExchangeRate() {
        //when+then
        assertNotNull(currencyService.getCurrencyExchangeRate("USD"));
        assertNotNull(currencyService.getCurrencyExchangeRate("EUR"));
    }

    @Test
    @DisplayName("Returns price rounded to two digits")
    void getPriceRoundedToTwoDigits() {
        //when
        double actualRoundedPrice = currencyService.getPriceRoundedToTwoDigits(8.87552);
        //then
        assertEquals(8.88, actualRoundedPrice);
    }

    @Test
    @DisplayName("Returns converted price in requested currency")
    void convert() {
        //prepare
        Double price = 123.45 / currencyService.getCurrencyExchangeRate("USD");
        Double expectedUsdAmount = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        //when
        Double actualUsdAmount = currencyService.convert(123.45, "USD");
        //then
        assertEquals(expectedUsdAmount, actualUsdAmount);
    }

    @Test
    @DisplayName("Returns converted price in requested currency")
    void convertThrowsIllegalArgumentException() {
        //when+then
        assertThrows(IllegalArgumentException.class, () -> {
            currencyService.convert(123.45, "UAH");
        });
    }
}