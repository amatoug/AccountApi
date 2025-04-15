package com.kata.bankapi.services;

import com.kata.bankapi.modele.BankAccount;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BankService {

    public static final List<BankAccount> createdAccounts = new ArrayList<>();

    public BankAccount createAccount(String name) {
        return new BankAccount(name);
    }

    public BankAccount getAccountById(UUID id) {
      return   createdAccounts.stream().filter(account -> account.getId().equals(id)).findFirst().orElse(null);
    }
}
