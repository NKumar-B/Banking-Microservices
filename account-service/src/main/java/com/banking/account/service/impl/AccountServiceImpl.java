package com.banking.account.service.impl;

import com.banking.account.dto.AccountDTO;
import com.banking.account.entity.Account;
import com.banking.account.exception.ResourceNotFoundException;
import com.banking.account.repository.AccountRepository;
import com.banking.account.service.AccountService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

        Account account = new Account();

        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setCustomerId(accountDTO.getCustomerId());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setStatus(accountDTO.getStatus());

        Account savedAccount = accountRepository.save(account);

        return convertToDTO(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {

        return accountRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {

        return accountRepository.findAll()
                .stream()
                .filter(account ->
                        account.getCustomerId().equals(customerId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public AccountDTO getAccountByIdForCustomer(Long id, Long customerId) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + id));

        if (!account.getCustomerId().equals(customerId)) {
            throw new RuntimeException(
                    "You are not authorized to access this account");
        }

        return convertToDTO(account);
    }

    @Override
    public AccountDTO getAccountById(Long id) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with id: " + id));

        return convertToDTO(account);
    }

    @Override
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {

        Account account = accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Account not found with id: " + id));

        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setCustomerId(accountDTO.getCustomerId());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setStatus(accountDTO.getStatus());

        Account updatedAccount = accountRepository.save(account);

        return convertToDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {

        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }

        accountRepository.deleteById(id);
    }

    private AccountDTO convertToDTO(Account account) {

        AccountDTO dto = new AccountDTO();

        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setCustomerId(account.getCustomerId());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());

        return dto;
    }
}