package com.api.ms_transaction.service.impl;

import com.api.ms_transaction.model.User;
import com.api.ms_transaction.repository.IUserRepository;
import com.api.ms_transaction.service.IUserService;
import org.springframework.stereotype.Service;

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
    public User findByUserName(String username) {
        return null;
    }

    @Override
    public void createUserTenant(User user) {
           userRepository.save(user);
    }

    @Override
    public Boolean validateUser(User user) {
        return null;
    }
}
