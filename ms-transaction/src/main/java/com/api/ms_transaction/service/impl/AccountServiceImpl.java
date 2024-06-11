package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.client.UserClient;
import com.api.ms_transaction.model.*;
import com.api.ms_transaction.repository.IAccountRepository;
import com.api.ms_transaction.repository.ITransactionRepository;
import com.api.ms_transaction.service.IAccountService;
import com.api.ms_transaction.service.ICurrencyAccountService;
import com.api.ms_transaction.service.IUserService;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
import java.util.*;

import static com.api.ms_transaction.model.CurrencyAccount.setCurrencyAccount;
import static com.api.ms_transaction.util.Converter.currencyConverter;
import static com.api.ms_transaction.util.InitCapital.INIT_CAPITAL;
import static com.api.ms_transaction.util.JwtUtil.extractPreferredUsername;


@Service
@Transactional
public class AccountServiceImpl implements IAccountService {
    private final IAccountRepository accountRepository;
    private final UserClient userClient;
    private final IUserService userService;
    private final ICurrencyAccountService currencyAccountService;

    private final ITransactionRepository transactionRepository;

    public AccountServiceImpl(IAccountRepository accountRepository, UserClient userClient, IUserService userService, ICurrencyAccountService currencyAccountService, ITransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.userClient = userClient;
        this.userService = userService;
        this.currencyAccountService = currencyAccountService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Account> findAll(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }


    @Override
    public void createAccount(String token) {

        User user;
        String username = extractPreferredUsername(token);
        List<UserRepresentation> userRepresentation = getUserFromFeignClient(username);
        if (username != null) {

            user = userService.instanciateUser(userRepresentation);

            user.setUserIdentifier(userRepresentation.get(0).getId());
            user.setUserName(userRepresentation.get(0).getUsername());
            user.setFirstName(userRepresentation.get(0).getFirstName());
            user.setLastName(userRepresentation.get(0).getLastName());
            user.setCountry(userRepresentation.get(0).firstAttribute("country"));

            Account account = new Account();
            AccountDetail accountDetail = new AccountDetail();
            CurrencyAccount currencyAccount = setCurrencyAccount(userRepresentation.get(0).firstAttribute("country"), currencyAccountService);

            accountDetail.setBalance(INIT_CAPITAL);
            accountDetail.setCreateAt(new DateTimeAtCreation(new Date()).getValue());
            accountDetail.setIsEnabled(true);
            accountDetail.setCreatedBy(userRepresentation.get(0).getUsername());
            accountDetail.setLastUpdateAt(new DateTimeAtCreation(new Date()).getValue());

            //relations

            currencyAccount.addAccountToCurrencyAccount(accountDetail);
            account.setAccountDetail(accountDetail);

            //relations
            user.addAccount(account);
            userService.createUserTenant(user);
        } else {
            throw new RuntimeException("Error creating new account");
        }

    }

    public List<UserRepresentation> getUserFromFeignClient(String username) {
        ResponseEntity<List<UserRepresentation>> response = userClient.getUserByUserName(username);
        return response.getBody();
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        return null;
    }

    @Override
    public void deleteAccount(Long id) {

    }

    @Override
    public BigDecimal processTransaction(String originCurrency, String targetCurrency, String sourceAccountRef, String destinationAccountRef, BigDecimal amount, String transactionType, String token) {

            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Must transfer a positive amount of money");
            }

            Account fromAccount;
            Account toAccount;
            BigDecimal newSourceBalance = null;

            fromAccount = validateAccount(sourceAccountRef,token);
            if(fromAccount.getAccountRef().isEmpty()){
                throw new RuntimeException("Source account not found");
            }
            toAccount = validateAccount(destinationAccountRef,token);
            if(toAccount.getAccountRef().isEmpty()){
                throw new RuntimeException("Destination account not found");
            }
            if(Objects.equals(targetCurrency, "") && Objects.equals(targetCurrency, "")){
                throw new RuntimeException("Currency does not exist");
            }

            if(fromAccount.getAccountDetail().getIsEnabled() && toAccount.getAccountDetail().getIsEnabled()){
                if(fromAccount.getAccountRef().equals(sourceAccountRef) && toAccount.getAccountRef().equals(destinationAccountRef)){
                    if (validationBalance(fromAccount,amount)) {

                        Transaction transaction = new Transaction();
                        transaction.setCurrencyOrigin(originCurrency);
                        transaction.setCurrencyDestination(targetCurrency);
                        transaction.setCreateAt(new DateTimeAtCreation(new Date()).getValue());
                        transaction.setAmount(amount);
                        transaction.setTransactionType(transactionType); // todo implement a method for validate internal transfer accounts from the same user or external different users
                        BigDecimal convertedValue =  currencyConverter(amount,originCurrency,targetCurrency);
                        transaction.setConvertedAmount(convertedValue);
                        transaction.setCreatedBy(userService.findUserByAccountRef(fromAccount.getAccountRef()).getUserName());

                        try{
                            BigDecimal currentFromBalance = fromAccount.getAccountDetail().getBalance();
                            fromAccount.getAccountDetail().setBalance(currentFromBalance.subtract(amount));
                            if(!Objects.equals(originCurrency, targetCurrency)  && isCurrencyCodeAvailable(targetCurrency,toAccount)) {
                                return setTransaction(fromAccount, toAccount, transaction, convertedValue);
                            }else if(!isCurrencyCodeAvailable(targetCurrency,toAccount)){
                                throw new RuntimeException("Currency is no available in this account");
                            }else {
                                return setTransaction(fromAccount, toAccount, transaction, amount);
                            }
                        }catch(Exception e){
                            e.getCause();
                        }

                    }else if(!validationBalance(fromAccount,amount)){
                        throw new RuntimeException("Insufficient founds");
                    }
                }
            }
            return null;
        }

    private BigDecimal setTransaction(Account fromAccount, Account toAccount,Transaction transaction, BigDecimal value) {
        BigDecimal currentToBalance = toAccount.getAccountDetail().getBalance();
        toAccount.getAccountDetail().setBalance(currentToBalance.add(value));
        transaction.setConvertedAmount(value);
        transaction.setStatus("1");
        fromAccount.getAccountDetail().add(transaction,fromAccount,toAccount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        return fromAccount.getAccountDetail().getBalance();
    }

    ;





    @Override
    public BigDecimal reloadFunds(String accountRef, BigDecimal amount, String token) {
        Account account = validateAccount(accountRef, token);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Reload a positive amount of money");
        }
        if (account.getAccountDetail().getIsEnabled()) {
            BigDecimal currentBalance = account.getAccountDetail().getBalance();
            account.getAccountDetail().setBalance(currentBalance.add(amount));
            accountRepository.save(account);
        } else {
            throw new RuntimeException("An error occurred while reloading the account");
        }
        return account.getAccountDetail().getBalance();
    }


    @Override
    public BigDecimal withdrawFunds(String accountRef, BigDecimal amount, String token) {
        Account account = validateAccount(accountRef, token);
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdraw a positive amount of money");
        }
        if (account.getAccountDetail().getIsEnabled()) {
            BigDecimal currentBalance = account.getAccountDetail().getBalance();
            if (currentBalance.compareTo(amount) < 0) {
                throw new RuntimeException("Insufficient founds");
            }
            account.getAccountDetail().setBalance(currentBalance.subtract(amount));
            accountRepository.save(account);
        } else {
            throw new RuntimeException("An error occurred while reloading the account");
        }
        return account.getAccountDetail().getBalance();
    }

    @Override
    public Account validateAccount(String accountRef, String token) {
        Account account = null;
        String username = extractPreferredUsername(token);
        User user = userService.findUserByAccountRef(accountRef);
        if (validateAccountByUsername(user.getUserName(), username)) {
            if (accountRepository.existsAccountByAccountRef(accountRef)) {
                account = accountRepository.findAccountByAccountRef(accountRef);
                if (account.getAccountRef().isEmpty()) {
                    throw new RuntimeException("Account does not exists in triwal app");
                }
            }
        }
        return account;
    }

    public Boolean validateAccountByUsername(String username, String tokenUsername) {
        boolean response = false;
        if (!username.isEmpty() && !tokenUsername.isEmpty()) {
            response = accountRepository.existsAccountByUsername(username) && username.equals(tokenUsername);
        }
        return response;
    }

    public Boolean validationBalance(Account account, BigDecimal amount) {
        return account.getAccountDetail().getBalance().compareTo(amount) >= 0;
    }

    @Override
    public Set<Account> getAccountsByUsername(String username, String tokenUsername) {
        Set<Account> accountSet = new HashSet<>();
        if (validateAccountByUsername(username,tokenUsername) && !username.isEmpty()) {
            accountSet = accountRepository.findAccountsByUsername(username);
        }
        return accountSet;
    }

    public Boolean isCurrencyCodeAvailable(String destinationCurrency, Account account){
        return account.getAccountDetail().getAccountCurrency().getCode().equals(destinationCurrency);
    }


}
