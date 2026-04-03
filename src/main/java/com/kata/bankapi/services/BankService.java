package com.kata.bankapi.services;

import com.kata.bankapi.exception.AccountNotFoundException;
import com.kata.bankapi.exception.AnauthorizedAmountException;
import com.kata.bankapi.exception.BankOverdraftAuthorizedExceededException;
import com.kata.bankapi.exception.UserNameMandatoryException;
import com.kata.bankapi.modele.BankAccount;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankService {

    /**
     * In-memory storage.
     *
     * - ConcurrentHashMap => thread-safe & O(1) lookup by id
     * - Keyed by UUID => avoids scanning a List with streams
     */
    private static final Map<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(String name) {
        validateName(name);

        BankAccount bankAccount = new BankAccount(name);
        accounts.put(bankAccount.getId(), bankAccount);
        return bankAccount;
    }

    public BankAccount getAccountById(UUID id) {
        if (id == null) {
            throw new AccountNotFoundException("account id is null");
        }

        BankAccount account = accounts.get(id);
        if (account == null) {
            throw new AccountNotFoundException("account not found");
        }

        return account;
    }

    public void deposit(BigDecimal amount, UUID id) {
        validateNonNegativeAmount(amount, "deposit");
        BankAccount account = getAccountById(id);

        // If BankAccount is accessed concurrently, you may want to synchronize on the account.
        account.setSolde(account.getSolde().add(amount));
    }

    public void withdraw(BigDecimal amount, UUID id) {
        validateNonNegativeAmount(amount, "withdraw");
        BankAccount account = getAccountById(id);

        if (account.getSolde().compareTo(amount) < 0) {
            throw new BankOverdraftAuthorizedExceededException("You have no overdraft protection");
        }

        // BUGFIX: subtract (min() was incorrect and would set the balance to the smallest of the two)
        account.setSolde(account.getSolde().subtract(amount));
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new UserNameMandatoryException("name is null or empty");
        }
    }

    private static void validateNonNegativeAmount(BigDecimal amount, String operation) {
        if (amount == null) {
            throw new AnauthorizedAmountException(operation + " amount is null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new AnauthorizedAmountException(operation + " amount is negative");
        }
    }
}