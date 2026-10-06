package com.banking.transaction.service.impl;

import com.banking.transaction.dto.TransactionDTO;
import com.banking.transaction.dto.AccountDTO;
import com.banking.transaction.entity.Transaction;
import com.banking.transaction.exception.ResourceNotFoundException;
import com.banking.transaction.repository.TransactionRepository;
import com.banking.transaction.service.TransactionService;
import com.banking.transaction.client.AccountClient;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    public TransactionServiceImpl(
            TransactionRepository transactionRepository,
            AccountClient accountClient) {

        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
    }

//    @Override
//    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
//
//        Transaction transaction = new Transaction();
//
//        transaction.setAccountId(transactionDTO.getAccountId());
//        transaction.setTransactionType(transactionDTO.getTransactionType());
//        transaction.setAmount(transactionDTO.getAmount());
//        transaction.setDescription(transactionDTO.getDescription());
//
//        // Automatically set transaction date
//        transaction.setTransactionDate(LocalDateTime.now());
//
//        Transaction savedTransaction =
//                transactionRepository.save(transaction);
//
//        return convertToDTO(savedTransaction);
//    }

    @Override
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {

        // Verify that the account exists
        AccountDTO account = accountClient.getAccountById(
                transactionDTO.getAccountId()
        );

        Transaction transaction = new Transaction();

        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction savedTransaction =
                transactionRepository.save(transaction);

        return convertToDTO(savedTransaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions() {

        return transactionRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getTransactionById(Long id) {

        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Transaction not found with id: " + id
                                ));

        return convertToDTO(transaction);
    }

    @Override
    public TransactionDTO updateTransaction(
            Long id,
            TransactionDTO transactionDTO) {

        Transaction transaction =
                transactionRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction not found with id: " + id
                                ));

        transaction.setAccountId(transactionDTO.getAccountId());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDescription(transactionDTO.getDescription());

        Transaction updatedTransaction =
                transactionRepository.save(transaction);

        return convertToDTO(updatedTransaction);
    }

    @Override
    public void deleteTransaction(Long id) {

        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Transaction not found with id: " + id
            );
        }

        transactionRepository.deleteById(id);
    }

    private TransactionDTO convertToDTO(Transaction transaction) {

        TransactionDTO dto = new TransactionDTO();

        dto.setId(transaction.getId());
        dto.setAccountId(transaction.getAccountId());
        dto.setTransactionType(transaction.getTransactionType());
        dto.setAmount(transaction.getAmount());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionDate(transaction.getTransactionDate());

        return dto;
    }
}