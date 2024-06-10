package com.msuser.msuser.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.msuser.msuser.dto.UserRegistrationDTO;
import com.msuser.msuser.service.IUserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/user")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_admin_client_role')")
    @GetMapping("/search")
    public ResponseEntity<?> findAllUsers() {
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_user_client_role') or hasRole('ROLE_admin_client_role')")
    @GetMapping("/search/{username}")
    public ResponseEntity<?> findUserByUserName(@PathVariable @Valid String username) {
        return new ResponseEntity<>(userService.searchUserByUsername(username), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ROLE_user_client_role') or hasRole('ROLE_admin_client_role')")
    @GetMapping("/search/internal")
    public ResponseEntity<?> getUserByUserName(@RequestParam @Valid String username) {
        return new ResponseEntity<>(userService.searchUserByUsername(username), HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('ROLE_user_client_role') or hasRole('ROLE_admin_client_role')")
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable @NotNull String userId, @RequestBody @Valid UserRegistrationDTO userRegistrationDTO)  {
        userService.updateUser(userId,userRegistrationDTO);
        return ResponseEntity.ok("User successfully updated");
    }

    @PreAuthorize("hasRole('ROLE_admin_client_role') or hasRole('ROLE_user_client_role') ")
    @DeleteMapping("delete/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable @NotNull String userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping ("/setStatus/{username}")
    public ResponseEntity<?> setEnableUserStatus(@PathVariable @Valid String username) {
        return new ResponseEntity<>(userService.setEnableUserStatus(username), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{userId}/send-verify-email")
    public void sendVerificationEmail(@PathVariable String userId) {
        userService.emailVerification(userId);
    }

    @GetMapping("/sendEmail/{email}/username/{username}")
    public void sendEmail(@PathVariable String email, @PathVariable @Valid String username) {
        userService.sendEmail(email, username);
    }


}
