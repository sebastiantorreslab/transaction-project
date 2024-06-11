package com.api.ms_transaction.repository;

import com.api.ms_transaction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    User findByUserName(String username);
    Boolean existsByUserName(String userName);

    @Query("""
    SELECT u FROM User u
    JOIN Account a ON a.user.id = u.id
    WHERE a.accountRef=:accountRef
""")
    User findUserByAccountRef(@Param("accountRef") String accountRef);





}
