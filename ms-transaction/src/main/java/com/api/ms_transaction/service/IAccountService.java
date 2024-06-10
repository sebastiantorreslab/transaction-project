package com.api.ms_transaction.service;

import com.api.ms_transaction.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;

public interface IAccountService {

    Account findById(Long  id);
    Page<Account> findAll(Pageable pageable);
    void createAccount(String token);
    Account updateAccount(Long  id, Account account); // todo: solo setea si la account está activa o no
    void deleteAccount(Long  id);
    void processTransaction(String sourceAccountId, String destinationAccountId, BigDecimal amount, String TransactionType); // todo possible strategy.
    void reloadAccount(String accountId, BigDecimal amount);
    void withdrawMoney(String accountId, BigDecimal amount);
    BigDecimal validateBalance(Account account, String userId);

    Boolean IsCreated(Account account);
}
