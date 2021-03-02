package com.hlushkov.movieland.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlushkov.movieland.common.NbuCurrencyRate;
import com.hlushkov.movieland.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DefaultCurrencyService implements CurrencyService {

    private final Map<String, Double> currencyMap = new ConcurrentHashMap<>();
    @Value("${current.nbu.rates.link}")
    private String nbuRatesLink;

    @Override
    public Double convert(Double costInUah, String currencyToConvertTo) {
        Double nbuExchangeRate = currencyMap.get(currencyToConvertTo);
        if (nbuExchangeRate != null) {
            return getPriceRoundedToTwoDigits(costInUah / nbuExchangeRate);
        } else  {
            throw new IllegalArgumentException("Unknown currency: ".concat(currencyToConvertTo));
        }
    }

    @PostConstruct
    @Scheduled(cron = "${currency.cron.update.time.period}")
    void updateCurrencyExchangeRates() {
        try {
            log.info("Refresh currency rates");
            ObjectMapper objectMapper = new ObjectMapper();

            URL rates = new URL(nbuRatesLink);
            URLConnection urlConnection = rates.openConnection();
            List<NbuCurrencyRate> nbuCurrencyRates = objectMapper.readValue(urlConnection.getInputStream(), new TypeReference<>() {
            });

            for (NbuCurrencyRate nbuCurrencyRate : nbuCurrencyRates) {
                currencyMap.put(nbuCurrencyRate.getCc(), nbuCurrencyRate.getRate());
            }
        } catch (Exception e) {
            log.error("Exception while updating currency rates: ", e);
        }
    }

    double getPriceRoundedToTwoDigits(double price) {
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getCurrencyExchangeRate(String currency) {
        return currencyMap.get(currency);
    }

}
