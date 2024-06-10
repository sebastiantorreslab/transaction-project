package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.Comission;
import com.api.ms_transaction.model.CurrencyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface ICurrencyAccountRepository extends JpaRepository<CurrencyAccount,Long> {

    Boolean existsCurrencyAccountByCountry(String country);

    CurrencyAccount findCurrencyAccountByCountry(String country);
}
