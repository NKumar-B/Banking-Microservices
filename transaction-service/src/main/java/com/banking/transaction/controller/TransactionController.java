package com.banking.transaction.controller;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.service.TransactionService;

import io.micrometer.core.instrument.config.validate.Validated;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO) {

        TransactionDTO createdTransaction =
                transactionService.createTransaction(transactionDTO);

        return new ResponseEntity<>(
                createdTransaction,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {

        return ResponseEntity.ok(
                transactionService.getAllTransactions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                transactionService.getTransactionById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDTO transactionDTO) {

        return ResponseEntity.ok(
                transactionService.updateTransaction(
                        id,
                        transactionDTO
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id) {

        transactionService.deleteTransaction(id);

        return ResponseEntity.noContent().build();
    }
}