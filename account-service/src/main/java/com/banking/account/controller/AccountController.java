package com.banking.account.controller;

import com.banking.account.dto.AccountDTO;
import com.banking.account.service.AccountService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(
            @Valid @RequestBody AccountDTO accountDTO) {

        AccountDTO createdAccount =
                accountService.createAccount(accountDTO);

        return new ResponseEntity<>(
                createdAccount,
                HttpStatus.CREATED
        );
    }

//    @GetMapping
//    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
//
//        return ResponseEntity.ok(
//                accountService.getAllAccounts()
//        );
//    }


    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        if ("ADMIN".equals(role)) {

            return ResponseEntity.ok(
                    accountService.getAllAccounts()
            );
        }

        return ResponseEntity.ok(
                accountService.getAccountsByCustomerId(userId)
        );
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<AccountDTO> getAccountById(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(
//                accountService.getAccountById(id)
//        );
//    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role) {

        if ("ADMIN".equals(role)) {

            return ResponseEntity.ok(
                    accountService.getAccountById(id)
            );
        }

        return ResponseEntity.ok(
                accountService.getAccountByIdForCustomer(id, userId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountDTO accountDTO) {

        return ResponseEntity.ok(
                accountService.updateAccount(id, accountDTO)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        return ResponseEntity.noContent().build();
    }
}