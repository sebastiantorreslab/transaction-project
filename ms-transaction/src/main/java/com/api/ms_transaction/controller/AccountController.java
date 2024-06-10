package com.api.ms_transaction.controller;

import com.api.ms_transaction.service.IAccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/account")
public class AccountController {
    private final IAccountService accountService;

    public AccountController( IAccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.substring(7);
        accountService.createAccount(token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getAllAccounts(@PageableDefault Pageable pageable){
       return new ResponseEntity<>(accountService.findAll(pageable), HttpStatus.ACCEPTED);
    }
}
