package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
@Repository
public interface IUserRepository extends JpaRepository<User,BigDecimal> {
    User findByUserName(String username);
}
