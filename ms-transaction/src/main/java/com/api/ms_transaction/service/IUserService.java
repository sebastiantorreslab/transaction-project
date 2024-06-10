package com.api.ms_transaction.service;

import com.api.ms_transaction.model.User;
import org.keycloak.representations.account.UserRepresentation;

import java.util.List;

public interface IUserService {

    User findById(Long id);
    Boolean existByUserName(String username);
    User findByUserName(String username);
    void createUserTenant(User user);
    User instanciateUser(List<UserRepresentation> userRepresentation);
    User findUserByAccountRef(String accountRef);

}
