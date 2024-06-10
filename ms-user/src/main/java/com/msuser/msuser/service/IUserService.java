package com.msuser.msuser.service;

import com.msuser.msuser.dto.UserRegistrationDTO;
import com.msuser.msuser.dto.UserResponseDTO;
import org.keycloak.representations.idm.UserRepresentation;

import java.io.IOException;
import java.util.List;

public interface IUserService {

    List<UserRepresentation> findAllUsers();

    List<UserRepresentation> searchUserByUsername(String username);

    String createUser(UserRegistrationDTO userDTO);

    UserRepresentation authenticateUser(String username, String password);

    void deleteUser(String userId);

    void updateUser(String userId, UserRegistrationDTO userDTO);

    UserResponseDTO setEnableUserStatus(String username);

    String resetPassword(String username);

    String logoutSession(String authorizationHeader) throws IOException;

    public void emailVerification(String userId);

    void sendEmail(String email, String username);
}
