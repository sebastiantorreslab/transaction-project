package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.Account;
import com.api.ms_transaction.model.Comission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface IComissionRepository extends JpaRepository<Comission,BigDecimal> {

}
