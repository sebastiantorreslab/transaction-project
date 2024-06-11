package com.api.ms_transaction.controller;

import com.api.ms_transaction.registry.request.transactionRequestDTO;
import com.api.ms_transaction.service.IAccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@CrossOrigin(origins = "http://localhost:3333")
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

    @PutMapping("/reload")
    public ResponseEntity<?> reloadAccount(@RequestParam("accountRef") String accountRef,@RequestParam("amount")BigDecimal amount,@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.substring(7);
        BigDecimal newBalance = accountService.reloadFunds(accountRef,amount,token);
        return new ResponseEntity<>("Successful reload, your new balance is "+ newBalance ,HttpStatus.ACCEPTED);

    }


    @PutMapping("/withdraw")
    public ResponseEntity<?> withdrawAccount(@RequestParam("accountRef") String accountRef,@RequestParam("amount") BigDecimal amount,@RequestHeader("Authorization") String tokenHeader){
        String token = tokenHeader.substring(7);
        BigDecimal newBalance = accountService.withdrawFunds(accountRef,amount,token);
        return new ResponseEntity<>("Successful withdrawal, your new balance is "+ newBalance ,HttpStatus.ACCEPTED);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> processTransaction(@RequestBody transactionRequestDTO transactionRequestDTO, @RequestHeader("Authorization") String tokenHeader ){
        String token = tokenHeader.substring(7);
        BigDecimal newBalance = accountService.processTransaction(transactionRequestDTO.currencyOrigin(),transactionRequestDTO.currencyDestination(),transactionRequestDTO.sourceAccountRef(),transactionRequestDTO.destinationAccountRef(),transactionRequestDTO.amount(),transactionRequestDTO.transactionType(),token);
    return new ResponseEntity<>("Transtaction succedfull you updtated balance is: " + newBalance,HttpStatus.ACCEPTED);
    }




}
