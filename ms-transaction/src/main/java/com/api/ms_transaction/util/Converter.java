package com.api.ms_transaction.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public abstract class Converter {
    private static final String COP = "COP";
    private static final String USD = "USD";
    private static final String EUR = "EUR";
    private static final String ARS = "ARS";

    public static Map<String, Map<String, BigDecimal>> getCurrenciesRates() {

        Map<String, BigDecimal> colombia = new HashMap<>();
        colombia.put(Converter.USD, BigDecimal.valueOf(3967));
        colombia.put(Converter.EUR, BigDecimal.valueOf(4286.64));
        colombia.put(Converter.ARS, BigDecimal.valueOf(4.39));

        Map<String, BigDecimal> usa = new HashMap<>();
        colombia.put(Converter.COP, BigDecimal.valueOf(0.00025));
        colombia.put(Converter.EUR, BigDecimal.valueOf(1.08));
        colombia.put(Converter.ARS, BigDecimal.valueOf(0.0011));

        Map<String, BigDecimal> argentina = new HashMap<>();
        colombia.put(Converter.USD, BigDecimal.valueOf(904.42));
        colombia.put(Converter.EUR, BigDecimal.valueOf(977.13));
        colombia.put(Converter.COP, BigDecimal.valueOf(0.23));

        Map<String, BigDecimal> europa = new HashMap<>();
        colombia.put(Converter.USD, BigDecimal.valueOf(0.92));
        colombia.put(Converter.COP, BigDecimal.valueOf(0.00023));
        colombia.put(Converter.ARS, BigDecimal.valueOf(0.0010));

        Map<String, Map<String, BigDecimal>> exchangeRates = new HashMap<>();
        exchangeRates.put(COP, colombia);
        exchangeRates.put(USD, usa);
        exchangeRates.put(EUR, europa);
        exchangeRates.put(ARS, argentina);

        return exchangeRates;

    }

    public static BigDecimal currencyConverter(BigDecimal amount, String originCurrency, String destinationCurrency) {

        Map<String, Map<String, BigDecimal>> exchangeRates = getCurrenciesRates();
        BigDecimal convertedAmount;
        if (originCurrency != null) {

            for (Map.Entry<String, Map<String, BigDecimal>> currencyEntry : exchangeRates.entrySet()) {
                String currency = currencyEntry.getKey();
                Map<String, BigDecimal> values = currencyEntry.getValue();

                //todo: implementar traductor de divisas nativo de java como en hacker ranck

                if (currency.equalsIgnoreCase(originCurrency) && values.containsKey(destinationCurrency)) {
                    convertedAmount = amount.divide(values.get(destinationCurrency), RoundingMode.HALF_UP);
                    return convertedAmount;
                }

            }

        }
        return null;
    }


}
