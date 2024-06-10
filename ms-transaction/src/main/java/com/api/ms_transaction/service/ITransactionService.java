package com.api.ms_transaction.service;

import com.api.ms_transaction.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionService {

    Transaction findById(Long id);
    Page<Transaction> findAll(Pageable pageable);
    Transaction createTransaction(Transaction transaction);
    Transaction updateTransaction(Long id, Transaction transaction);
    void deleteTransaction(Long id);
    Boolean IsSuccess(Transaction transaction);
}
