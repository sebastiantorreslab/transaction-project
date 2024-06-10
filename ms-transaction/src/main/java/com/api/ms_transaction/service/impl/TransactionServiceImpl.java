package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.model.Transaction;
import com.api.ms_transaction.service.ITransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionServiceImpl implements ITransactionService {
    @Override
    public Transaction findById(Long id) {
        return null;
    }

    @Override
    public Page<Transaction> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public Transaction updateTransaction(Long id, Transaction transaction) {
        return null;
    }

    @Override
    public void deleteTransaction(Long id) {

    }

    @Override
    public Boolean IsSuccess(Transaction transaction) {
        return null;
    }
}
