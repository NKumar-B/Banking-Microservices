package com.banking.account.service;

import com.banking.account.dto.AccountDTO;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    List<AccountDTO> getAllAccounts();

    AccountDTO getAccountById(Long id);

    AccountDTO updateAccount(Long id, AccountDTO accountDTO);

    List<AccountDTO> getAccountsByCustomerId(Long customerId);

    AccountDTO getAccountByIdForCustomer(Long id, Long customerId);

    void deleteAccount(Long id);
}