package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.model.CurrencyAccount;
import com.api.ms_transaction.repository.ICurrencyAccountRepository;
import com.api.ms_transaction.service.ICurrencyAccountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CurrencyAccountServiceImpl implements ICurrencyAccountService {

    private final ICurrencyAccountRepository currencyAccountRepository;

    public CurrencyAccountServiceImpl(ICurrencyAccountRepository currencyAccountRepository) {
        this.currencyAccountRepository = currencyAccountRepository;
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal amount, String originCurrency, String destinationCurrency) {
        return null;
    }

    @Override
    public Boolean existsCurrencyAccountByCountry(String country) {
        return currencyAccountRepository.existsCurrencyAccountByCountry(country.toLowerCase());
    }

    @Override
    public CurrencyAccount findCurrencyAccountByCountry(String country) {
        return currencyAccountRepository.findCurrencyAccountByCountry(country.toLowerCase());
    }

    @Override
    public void create(CurrencyAccount currencyAccount) {
        currencyAccountRepository.save(currencyAccount);

    }
}
