package com.api.ms_transaction.util;

import com.api.ms_transaction.model.Account;
import com.api.ms_transaction.model.Transaction;
import com.api.ms_transaction.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@EqualsAndHashCode
@Getter
@Setter
public abstract class AccountManagement {

    public static void tenantsManage(User user){
        Map<String, Set<Account>> tenantsMap = new HashMap<>();
        if(user != null){
            tenantsMap.put(user.getUserName(),user.getAccounts());
        }
    }

    public static void transactionsManage(Account account){
        Set<Transaction> accountTransactions = new HashSet<>();
        Map<Long, Set<Transaction>> transactions = new HashMap<>();

        if(account != null){
            Set<Transaction> incomingTransactions = account.getAccountDetail().getIncomingTransactions();
            Set<Transaction> outgoingTransactions = account.getAccountDetail().getOutgoingTransactions();
            accountTransactions.addAll(incomingTransactions);
            accountTransactions.addAll(outgoingTransactions);
            transactions.put(account.getId(),accountTransactions);
        }
    }





}
