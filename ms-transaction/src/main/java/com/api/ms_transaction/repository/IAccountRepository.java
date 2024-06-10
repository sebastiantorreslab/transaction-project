package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface IAccountRepository extends JpaRepository<Account,Long> {
}
