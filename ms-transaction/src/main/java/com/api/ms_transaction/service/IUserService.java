package com.api.ms_transaction.service;

import com.api.ms_transaction.model.User;

public interface IUserService {

    User findById(Long id);
    User findByUserName(String username);
    void createUserTenant(User user);
    Boolean validateUser(User user); // todo: exist by id and it not null and not blank
}
