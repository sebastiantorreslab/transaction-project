package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.model.User;
import com.api.ms_transaction.repository.IUserRepository;
import com.api.ms_transaction.service.IUserService;
import org.keycloak.representations.account.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public Boolean existByUserName(String username) {
        return userRepository.existsByUserName(username);
    }

    @Override
    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }


    @Override
    public void createUserTenant(User user) {
        userRepository.save(user);
    }


    public User instanciateUser(List<UserRepresentation> userRepresentation) {
        User user;
        if (validateUser(userRepresentation)) {
            user = this.findByUserName(userRepresentation.get(0).getUsername());
            return user;
        } else {
            user = new User();
            return user;
        }
    }


    public Boolean validateUser(List<UserRepresentation> userRepresentation) {
        return this.existByUserName(userRepresentation.get(0).getUsername());
    }




}
