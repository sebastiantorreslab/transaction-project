package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Set;

@Repository
public interface IAccountRepository extends JpaRepository<Account,Long> {

    Boolean existsAccountByAccountRef(String accountRef);
    Account findAccountByAccountRef(String accountRef);

    @Query("""
            SELECT a FROM Account a 
            JOIN a.user u 
            WHERE u.userName = :username
""")
    Set<Account> findAccountsByUsername(@Param("username") String username);


    @Query("""
        SELECT\s
        CASE WHEN (COUNT(a.accountRef) > 0)\s
        THEN true\s
        ELSE false\s
        END\s
        FROM Account a\s
        WHERE EXISTS (
            SELECT 1 FROM User u\s
            WHERE u.id = a.user.id\s
            AND u.userName = :username
           
        )
    """)
    Boolean existsAccountByUsername(@Param("username") String username);


}
