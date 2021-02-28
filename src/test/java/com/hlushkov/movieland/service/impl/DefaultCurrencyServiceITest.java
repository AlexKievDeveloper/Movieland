package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.Currency;
import com.hlushkov.movieland.config.TestWebContextConfiguration;
import com.hlushkov.movieland.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
@TestWebContextConfiguration
class DefaultCurrencyServiceITest {
    @Autowired
    private CurrencyService currencyService;

    @Test
    void getCurrencyExchangeRate() {
        //when+then
        assertNotNull(currencyService.getCurrencyExchangeRate(Currency.USD));
        assertNotNull(currencyService.getCurrencyExchangeRate(Currency.EUR));
    }
}