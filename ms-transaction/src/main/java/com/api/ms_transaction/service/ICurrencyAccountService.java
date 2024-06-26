package com.api.ms_transaction.service;

import com.api.ms_transaction.model.CurrencyAccount;

import java.math.BigDecimal;

public interface ICurrencyAccountService {

    BigDecimal convertCurrency(BigDecimal amount, String originCurrency, String destinationCurrency);

    Boolean existsCurrencyAccountByCountry(String country);

    CurrencyAccount findCurrencyAccountByCountry(String country);

    void create(CurrencyAccount currencyAccount);
}
