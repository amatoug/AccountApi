package com.kata.bankapi.services;

import com.kata.bankapi.exception.AccountNotFoundException;
import com.kata.bankapi.exception.AnauthorizedAmountException;
import com.kata.bankapi.exception.BankOverdraftAuthorizedExceededException;
import com.kata.bankapi.exception.UserNameMandatoryException;
import com.kata.bankapi.modele.BankAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    public static final List<BankAccount> createdAccounts = new ArrayList<>();


    public BankAccount createAccount(String name) {
        if (name == null || name.isEmpty()) {
            throw new UserNameMandatoryException("name is null or empty");
        }
        BankAccount bankAccount = new BankAccount(name);
        createdAccounts.add(bankAccount);
        return bankAccount;
    }

    public BankAccount getAccountById(UUID id) {
        return createdAccounts.stream().filter(account -> account.getId().equals(id)).findFirst().orElseThrow( () ->new AccountNotFoundException("account not found") );
    }

    public void deposit(BigDecimal depot, UUID id) {
        BankAccount bankAccount = getAccountById(id);
        if (depot.compareTo(BigDecimal.ZERO) < 0) {
            throw new AnauthorizedAmountException("deposit amount is negative");
        }
        bankAccount.setSolde(bankAccount.getSolde().add(depot));
    }

    public void withdraw(BigDecimal withdraw, UUID id) {
        BankAccount bankAccount = getAccountById(id);
        if (withdraw.compareTo(BigDecimal.ZERO) < 0) {
            throw new AnauthorizedAmountException("withdraw amount is negative");
        }
        if (bankAccount.getSolde().compareTo(withdraw) < 0) {
            throw new BankOverdraftAuthorizedExceededException("You have no overdraft protection");
        }
        bankAccount.setSolde(bankAccount.getSolde().min(withdraw));
    }
}
