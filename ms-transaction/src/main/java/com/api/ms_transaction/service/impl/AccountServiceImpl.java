package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.client.UserClient;
import com.api.ms_transaction.configuration.security.JwtAuthConverter;
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

import javax.print.attribute.standard.DateTimeAtCreation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.api.ms_transaction.model.CurrencyAccount.setCurrencyAccount;
import static com.api.ms_transaction.util.InitCapital.INIT_CAPITAL;
import static com.api.ms_transaction.util.JwtUtil.extractPreferredUsername;


@Service
public class AccountServiceImpl implements IAccountService {


    private final IAccountRepository accountRepository;

    private final UserClient userClient;

    private final IUserService userService;


    private final ICurrencyAccountService currencyAccountService;

    private final JwtAuthConverter jwtAuthConverter;

    public AccountServiceImpl(IAccountRepository accountRepository, UserClient userClient, IUserService userService, ICurrencyAccountService currencyAccountService, JwtAuthConverter jwtAuthConverter) {
        this.accountRepository = accountRepository;
        this.userClient = userClient;
        this.userService = userService;
        this.currencyAccountService = currencyAccountService;
        this.jwtAuthConverter = jwtAuthConverter;
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
            CurrencyAccount currencyAccount = setCurrencyAccount(userRepresentation.get(0).firstAttribute("country"),currencyAccountService);


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
    public void processTransaction(String sourceAccountId, String destinationAccountId, BigDecimal amount, String TransactionType) {

    }

    @Override
    public void reloadAccount(String accountId, BigDecimal amount) {

    }

    @Override
    public void withdrawMoney(String accountId, BigDecimal amount) {

    }

    @Override
    public BigDecimal validateBalance(Account account, String userId) {
        return null;
    }

    @Override
    public Boolean IsCreated(Account account) {
        return null;
    }
}
