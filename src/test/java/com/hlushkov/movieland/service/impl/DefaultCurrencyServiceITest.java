package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.config.TestWebContextConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    @DisplayName("Returns converted price in requested currency")
    void convert() {
        //prepare
        Double expectedUsdAmount = 123.45 / currencyService.getCurrencyExchangeRate("USD");
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