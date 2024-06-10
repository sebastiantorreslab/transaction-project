package com.msuser.msuser.controller;

import com.msuser.msuser.dto.DataResponseToken;
import com.msuser.msuser.dto.UserRegistrationDTO;
import com.msuser.msuser.dto.UserResponseDTO;
import com.msuser.msuser.service.IUserService;
import com.msuser.msuser.util.TokenProvider;
import jakarta.validation.Valid;
import lombok.Getter;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/auth")
public class AuthController {

    private final IUserService userService;
    private final TokenProvider tokenProvider;

    public AuthController(IUserService userService, TokenProvider tokenProvider) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO)  {
        String response = userService.createUser(userRegistrationDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws IOException {
        UserRepresentation user = userService.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (user != null) {
            String accessToken = tokenProvider.requestToken(loginRequest.getUsername(), loginRequest.getPassword());
            UserResponseDTO userDTO = new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.isEmailVerified(),
                    user.isEnabled(), user.getAttributes().toString());
            return ResponseEntity.ok(new DataResponseToken(userDTO, accessToken));
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }


    }
    
    @PutMapping ("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam String userEmail) {
    	String result = userService.resetPassword(userEmail);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/logout", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<?> logout(@RequestParam("refresh_token") @Valid String refreshToken) throws IOException {
        userService.logoutSession(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @Getter
    public static class LoginRequest {

        private String username;
        private String password;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }
}
