package com.msuser.msuser.service.impl;

import com.msuser.msuser.dto.UserRegistrationDTO;
import com.msuser.msuser.dto.UserResponseDTO;
import com.msuser.msuser.service.IUserService;
import com.msuser.msuser.util.TokenProvider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.msuser.msuser.util.keycloakProvider.getRealmResource;
import static com.msuser.msuser.util.keycloakProvider.getUserResource;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

    private final TokenProvider tokenProvider;
    private final EmailService emailService;

    public UserServiceImpl(TokenProvider tokenProvider, EmailService emailService) {
        this.tokenProvider = tokenProvider;
        this.emailService = emailService;
    }


    /*
     * Method for list user by its own username
     * @return List<UserRepresentation>
     * */
    @Override
    public List<UserRepresentation> findAllUsers() {
        return getRealmResource().users().list();
    }


    /*
     * Method for create a new user in keycloak
     * @return String
     * */
    @Override
    public List<UserRepresentation> searchUserByUsername(String username) {
        return getRealmResource().users().searchByUsername(username, true);
    }

    //todo: add method to validate: a user only can request information about his own user.


    /*
     * Method for create a new user in keycloak
     * @return String
     * */
    @Override
    public String createUser(@NonNull UserRegistrationDTO userRegistrationDTO) {
        int status = 0;

        UsersResource userResource = getUserResource();

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userRegistrationDTO.firstName());
        userRepresentation.setLastName(userRegistrationDTO.lastName());
        userRepresentation.setEmail(userRegistrationDTO.email());
        userRepresentation.setUsername(userRegistrationDTO.username());
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("country", Collections.singletonList(userRegistrationDTO.country()));
        userRepresentation.setAttributes(attributes);
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(true);

        Response response = userResource.create(userRepresentation);
        status = response.getStatus();

        if (status == 201) {
            String path = response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf("/") + 1);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userRegistrationDTO.pass());

            userResource.get(userId).resetPassword(credentialRepresentation);

            RealmResource realmResource = getRealmResource();
            List<RoleRepresentation> roleRepresentations = null;


            if (userRegistrationDTO.roles() == null || userRegistrationDTO.roles().isEmpty()) {
                roleRepresentations = List.of(realmResource.roles().get("user-role-realm").toRepresentation());
            } else {
                roleRepresentations = realmResource.roles().list()
                        .stream().filter(role -> userRegistrationDTO.roles()
                                .stream()
                                .anyMatch(roleName -> roleName.equalsIgnoreCase(role.getName())))
                        .toList();
            }

            realmResource.users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);

            List<UserRepresentation> representationList = userResource.searchByUsername(userRegistrationDTO.username(), true);

            if (!representationList.isEmpty()) {
                UserRepresentation userRep = representationList.stream()
                        .filter(ur -> !ur.isEmailVerified())
                        .findFirst()
                        .orElse(null);

                if (userRep != null) {
                    emailVerification(userRep.getId());
                }
            }


        } else if (status == 409) {
            log.error("User already exist");
            return "User already exist";
        } else {
            log.error("Error creating user");
            return "Error creating user";
        }
        return null;
    }


    @Override
    public void deleteUser(String userId) {
        getUserResource()
                .get(userId)
                .remove();
    }

    /*
     * Method for update a user in keycloak
     * @return void
     * */
    @Override
    public void updateUser(String userId, UserRegistrationDTO userRegistrationDTO) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userRegistrationDTO.pass());

        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setFirstName(userRegistrationDTO.firstName());
        userRepresentation.setLastName(userRegistrationDTO.lastName());
        userRepresentation.setEmail(userRegistrationDTO.email());
        userRepresentation.setUsername(userRegistrationDTO.username());
        Map<String, List<String>> attributes = new HashMap<>();
        attributes.put("country", Collections.singletonList(userRegistrationDTO.country()));
        userRepresentation.setAttributes(attributes);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = getUserResource().get(userId);
        userResource.update(userRepresentation);

    }

    @Override
    public UserResponseDTO setEnableUserStatus(String username) {

        List<UserRepresentation> userRepresentation = searchUserByUsername(username);
        userRepresentation.forEach(user -> user.setEnabled(!user.isEnabled()));
        UserResource userResource = getUserResource().get(userRepresentation.get(0).getId());
        userResource.update(userRepresentation.get(0));
        return new UserResponseDTO(
                userRepresentation.get(0).getId(),
                userRepresentation.get(0).getUsername(),
                userRepresentation.get(0).getFirstName(),
                userRepresentation.get(0).getLastName(),
                userRepresentation.get(0).getEmail(),
                userRepresentation.get(0).isEmailVerified(),
                userRepresentation.get(0).isEnabled(),
                userRepresentation.get(0).getAttributes().toString());
    }

    public UserRepresentation authenticateUser(String username, String password) {
        UsersResource usersResource = getUserResource();
        List<UserRepresentation> users = usersResource.search(username);
        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }


    @Override
    public String resetPassword(String userEmail) {
        try {
            UsersResource usersResource = getUserResource();
            List<UserRepresentation> representationList = usersResource.searchByEmail(userEmail, true);
            UserRepresentation userRepresentation = representationList.stream().findFirst().orElse(null);

            if (userRepresentation != null) {
                String userId = userRepresentation.getId();
                tokenProvider.sendResetPasswordEmail(userId);
                return "Link para restablecer contraseña enviado.";
            } else {
                return "No se encontró ningún usuario con el correo electrónico proporcionado.";
            }
        } catch (Exception e) {
            return "Se produjo un error al intentar restablecer la contraseña.";
        }

    }

    @Override
    public String logoutSession(@NotNull @NotBlank String refreshToken) throws IOException {
        if (!refreshToken.isEmpty()) {
            return tokenProvider.requestLogout(refreshToken);
        }
        return "logout failed";
    }

    @Override
    public void emailVerification(String userId) {
        UsersResource usersResource = getUserResource();
        usersResource.get(userId).sendVerifyEmail();
    }

    @Override
    public void sendEmail(String email, String username) {
        emailService.sendActivationEmail(email, username);
    }
}
