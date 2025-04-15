package com.kata.bankapi.service;

import com.kata.bankapi.modele.BankAccount;
import com.kata.bankapi.services.BankService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BankServiceTest {

    private BankService bankService;

    @BeforeEach
    void init() {
        bankService = new BankService();
    }

    @Test
    void should_create_account() {
        BankAccount bankAccount = bankService.createAccount("Ali");
        Assertions.assertThat(bankAccount).isNotNull();
        Assertions.assertThat(bankAccount.getName()).isEqualTo("Ali");
    }

    @Test
    void should_create_three_accounts() {
        List<String> ownersNames = Arrays.asList("Alice", "Bob", "Charlie");
        List<BankAccount> createdAccounts = new ArrayList<>();
        ownersNames.forEach(name -> createdAccounts.add(bankService.createAccount(name)));


        Assertions.assertThat(createdAccounts).isNotNull();
        Assertions.assertThat(createdAccounts.size()).isEqualTo(3);
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(0).getId()).getName()).isEqualTo("Alice");
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(1).getId()).getName()).isEqualTo("Bob");
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(2).getId()).getName()).isEqualTo("Charlie");

    }
}
