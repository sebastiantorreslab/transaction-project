package com.api.ms_transaction.service;

import java.math.BigDecimal;

public interface ICurrencyAccountService {

    BigDecimal convertCurrency(BigDecimal amount, String originCurrency, String destinationCurrency);
}
