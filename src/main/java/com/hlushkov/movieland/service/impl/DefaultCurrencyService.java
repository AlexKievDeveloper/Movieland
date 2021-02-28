package com.hlushkov.movieland.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlushkov.movieland.common.Currency;
import com.hlushkov.movieland.common.NbuCurrencyRate;
import com.hlushkov.movieland.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DefaultCurrencyService implements CurrencyService {

    private final Map<Currency, Double> currencyDoubleMap = new ConcurrentHashMap<>();

    @Override
    public Double getCurrencyExchangeRate(Currency currency) {
        return currencyDoubleMap.get(currency);
    }

    @PostConstruct
    @Scheduled(cron = "${currency.cron.update.time.period}")
    void updateCurrencyExchangeRates() throws IOException {
        log.info("Refresh currency rates");
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyyMMdd");

        String requestForEurRateForCurrentData = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?" +
                "valcode=EUR&date=" + formatters.format(LocalDate.now()) + "&json";
        String requestForUsdRateForCurrentData = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?" +
                "valcode=USD&date=" + formatters.format(LocalDate.now()) + "&json";

        URL eurUrl = new URL(requestForEurRateForCurrentData);
        URLConnection urlEurConnection = eurUrl.openConnection();
        List<NbuCurrencyRate> eurNbuRate = objectMapper.readValue(urlEurConnection.getInputStream(),
                new TypeReference<>() {
                });

        URL usdUrl = new URL(requestForUsdRateForCurrentData);
        URLConnection urlUsdConnection = usdUrl.openConnection();
        List<NbuCurrencyRate> usdNbuRate = objectMapper.readValue(urlUsdConnection.getInputStream(),
                new TypeReference<>() {
                });

        currencyDoubleMap.put(Currency.USD, usdNbuRate.get(0).getRate());
        currencyDoubleMap.put(Currency.EUR, eurNbuRate.get(0).getRate());
    }

}
