package com.hlushkov.movieland.web.util;

import com.hlushkov.movieland.common.Currency;
import org.springframework.core.convert.converter.Converter;

public class CurrencyRequestParameterConverter implements Converter<String, Currency> {
    @Override
    public Currency convert(String currencyParameterValue) {
        return Currency.getCurrency(currencyParameterValue);
    }
}
