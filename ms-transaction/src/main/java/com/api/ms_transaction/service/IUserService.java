package com.api.ms_transaction.service;

import com.api.ms_transaction.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    User findById(Long id);
    User findByUserName(String username);
    Page<User> findAll(Pageable pageable);
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Boolean validateUser(User user); // todo: exist by id and it not null and not blank
}
