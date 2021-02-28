package com.hlushkov.movieland.common;

public enum Currency {
    USD("usd"), EUR("eur"), UAH("uah");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Currency getCurrency(String name) {
        Currency[] currencies = Currency.values();
        for (Currency currency : currencies) {
            if (currency.getName().equals(name)) {
                return currency;
            }
        }

        throw new IllegalArgumentException("No currency for name ".concat(name));
    }
}
