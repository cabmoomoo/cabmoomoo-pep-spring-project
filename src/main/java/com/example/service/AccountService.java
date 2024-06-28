package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.*;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account) throws DupilcateAccountException {
        if (account.getUsername().isBlank()) {
            return null;
        }
        if (account.getPassword().length() < 4) {
            return null;
        }
        if (accountRepository.findAccountByUsername(account.getUsername()) != null) {
            throw new DupilcateAccountException();
        }
        return accountRepository.save(account);
    }

    public Account processLogin(Account account) {
        return accountRepository.findAccountByUsernameAndPassword(account.getUsername(), account.getPassword());
    }

    public Account getAccountByID(int userID) {
        return accountRepository.findById(userID).orElse(null);
    }

}
