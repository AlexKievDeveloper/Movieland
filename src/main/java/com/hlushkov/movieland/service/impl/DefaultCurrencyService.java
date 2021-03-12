package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.NbuCurrencyRate;
import com.hlushkov.movieland.service.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
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
            return costInUah / nbuExchangeRate;
        } else {
            throw new IllegalArgumentException("Unknown currency: ".concat(currencyToConvertTo));
        }
    }

    @PostConstruct
    @Scheduled(cron = "${currency.cron.update.time.period}")
    void updateCurrencyExchangeRates() {
        try {
            log.info("Refresh currency rates");

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<List<NbuCurrencyRate>> response = restTemplate.exchange(nbuRatesLink,
                    HttpMethod.GET,
                    httpEntity,
                    new ParameterizedTypeReference<>() {
                    }
            );

            List<NbuCurrencyRate> nbuCurrencyRates = response.getBody();

            for (NbuCurrencyRate nbuCurrencyRate : nbuCurrencyRates) {
                log.debug("Currency rate: {}", nbuCurrencyRate);
                currencyMap.put(nbuCurrencyRate.getName(), nbuCurrencyRate.getRate());
            }
        } catch (Exception e) {
            log.error("Exception while updating currency rates: ", e);
        }
    }

    public Double getCurrencyExchangeRate(String currency) {
        return currencyMap.get(currency);
    }

}
