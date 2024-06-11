package com.api.ms_transaction.service;

import com.api.ms_transaction.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;

import java.math.BigDecimal;
import java.util.Set;

public interface IAccountService {

    Account findById(Long  id);
    Page<Account> findAll(Pageable pageable);
    void createAccount(String token);
    Account updateAccount(Long  id, Account account); // todo: solo setea si la account está activa o no
    void deleteAccount(Long  id);
    void processTransaction(String sourceAccountRef, String destinationAccountRef, BigDecimal amount, String TransactionType); // todo possible strategy.
    BigDecimal reloadFunds(String accountRef, BigDecimal amount,String token);
    BigDecimal withdrawFunds(String accountRef, BigDecimal amount,String token);
    Account  validateAccount(String accountRef,String token);
    Set<Account> getAccountsByUsername(String username, String tokenUsername);
    Boolean validateAccountByUsername(String username, String tokenUsername);



}
