package com.api.ms_transaction.service;

import com.api.ms_transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ITransactionService {

    Transaction findById(BigDecimal id);
    Page<Transaction> findAll(Pageable pageable);
    Transaction createTransaction(Transaction transaction);
    Transaction updateTransaction(BigDecimal id, Transaction transaction);
    void deleteTransaction(BigDecimal id);
    Boolean IsSuccess(Transaction transaction);
}
