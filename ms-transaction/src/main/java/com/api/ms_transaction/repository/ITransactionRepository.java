package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.CurrencyAccount;
import com.api.ms_transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,BigDecimal> {
}
