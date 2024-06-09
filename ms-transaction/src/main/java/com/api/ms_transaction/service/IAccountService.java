package com.api.ms_transaction.service;

import com.api.ms_transaction.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface IAccountService {

    Account findById(BigDecimal  id);
    Page<Account> findAll(Pageable pageable);
    Account createAccount(Account account);
    Account updateAccount(BigDecimal  id, Account account); // todo: solo setea si la account está activa o no
    void deleteAccount(BigDecimal  id);
    void processTransaction(BigDecimal  sourceAccountId, BigDecimal destinationAccountId, BigDecimal amount, String TransactionType); // todo possible strategy.
    void reloadAccount(BigDecimal accountId, BigDecimal amount);
    void withdrawMoney(BigDecimal accountId, BigDecimal amount);
    BigDecimal validateBalance(Account account, String userId);

    Boolean IsCreated(Account account);
}
