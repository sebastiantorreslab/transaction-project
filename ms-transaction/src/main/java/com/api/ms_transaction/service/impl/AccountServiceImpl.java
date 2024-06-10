package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.client.UserClient;
import com.api.ms_transaction.model.Account;
import com.api.ms_transaction.model.AccountDetail;
import com.api.ms_transaction.model.CurrencyAccount;
import com.api.ms_transaction.model.User;
import com.api.ms_transaction.repository.IAccountRepository;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.api.ms_transaction.model.CurrencyAccount.setCurrencyAccount;
import static com.api.ms_transaction.util.InitCapital.INIT_CAPITAL;
import static com.api.ms_transaction.util.JwtUtil.extractPreferredUsername;


@Service
@Transactional
public class AccountServiceImpl implements IAccountService {

    private final IAccountRepository accountRepository;
    private final UserClient userClient;
    private final IUserService userService;
    private final ICurrencyAccountService currencyAccountService;

    public AccountServiceImpl(IAccountRepository accountRepository, UserClient userClient, IUserService userService, ICurrencyAccountService currencyAccountService) {
        this.accountRepository = accountRepository;
        this.userClient = userClient;
        this.userService = userService;
        this.currencyAccountService = currencyAccountService;
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
    public void processTransaction(String sourceAccountRef, String destinationAccountRef, BigDecimal amount, String TransactionType) {

    }

    @Override
    public void reloadAccount(String accountRef, BigDecimal amount) {
        Account account = validateAccount(accountRef);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Reload a positive amount of money");
        }
        if (account.getAccountDetail().getIsEnabled()) {
            BigDecimal currentBalance = account.getAccountDetail().getBalance();
            account.getAccountDetail().setBalance(currentBalance.add(amount));
            accountRepository.save(account);
        } else {
            throw new RuntimeException("An error occurred while reloading the account");
        }
    }


    @Override
    public void withdrawMoney(String accountRef, BigDecimal amount) {
        Account account = validateAccount(accountRef);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
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
    }

    @Override
    public Account validateAccount(String accountRef) {
        Account account = null;
        User user = userService.findUserByAccountRef(accountRef);
        if (validateAccountByUsername(user.getUserName())) {
            if (accountRepository.existsAccountByAccountRef(accountRef)) {
                account = accountRepository.findAccountByAccountRef(accountRef);
                if (account.getAccountRef().isEmpty()) {
                    throw new RuntimeException("Account does not exists in triwal app");
                }
            }
        }
        return account;
    }

    public Boolean validateAccountByUsername(String username) {
        return accountRepository.existsAccountByUsername(username) && !username.isEmpty();
    }


    @Override
    public Set<Account> getAccountsByUsername(String username) {
        Set<Account> accountSet = new HashSet<>();
        if (validateAccountByUsername(username) && !username.isEmpty()) {
            accountSet = accountRepository.findAccountsByUsername(username);
        }
        ;
        return accountSet;
    }


}
