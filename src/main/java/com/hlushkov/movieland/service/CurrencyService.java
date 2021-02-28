package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.Currency;

public interface CurrencyService {
    Double getCurrencyExchangeRate(Currency currency);
}
