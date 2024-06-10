package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.Account;
import com.api.ms_transaction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    User findByUserName(String username);
    Boolean existsByUserName(String userName);

    @Query("""
    SELECT u.userName FROM User u
    JOIN Account a
    WHERE a.accountRef =: accountRef
""")
    User findUserByAccountRef(@Param("accountRef") String accountRef);





}
