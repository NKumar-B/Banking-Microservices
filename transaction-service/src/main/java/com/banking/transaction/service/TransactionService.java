package com.banking.transaction.service;

import com.banking.transaction.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {

    TransactionDTO createTransaction(TransactionDTO transactionDTO);

    List<TransactionDTO> getAllTransactions();

    TransactionDTO getTransactionById(Long id);

    TransactionDTO updateTransaction(Long id, TransactionDTO transactionDTO);

    void deleteTransaction(Long id);
}