package com.kata.bankapi.service;

import com.kata.bankapi.exception.AccountNotFoundException;
import com.kata.bankapi.exception.AnauthorizedAmountException;
import com.kata.bankapi.exception.BankOverdraftAuthorizedExceededException;
import com.kata.bankapi.exception.UserNameMandatoryException;
import com.kata.bankapi.modele.BankAccount;
import com.kata.bankapi.services.BankService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    @Test
    void should_create_eleven_accounts() {
        List<String> ownersNames = Arrays.asList("Alice", "Bob", "Charlie", "Test1", "toto", "titi", "tata", "lulu", "lolo", "lala", "lilou");
        List<BankAccount> createdAccounts = new ArrayList<>();
        ownersNames.forEach(name -> createdAccounts.add(bankService.createAccount(name)));


        Assertions.assertThat(createdAccounts).isNotNull();
        Assertions.assertThat(createdAccounts.size()).isEqualTo(11);
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(0).getId()).getName()).isEqualTo("Alice");
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(1).getId()).getName()).isEqualTo("Bob");
        Assertions.assertThat(bankService.getAccountById(createdAccounts.get(2).getId()).getName()).isEqualTo("Charlie");

    }

    @Test
    void should_throw_exception_when_create_account_without_name() {
        Assertions.assertThatThrownBy(() -> bankService.createAccount(null)).isInstanceOf(UserNameMandatoryException.class);
    }

    @Test
    void should_deposit_amount() {
        BigDecimal depot = BigDecimal.valueOf(100);
        UUID id = bankService.createAccount("Ali").getId();
        bankService.deposit(depot,id);
        Assertions.assertThat(bankService.getAccountById(id).getSolde()).isEqualTo(depot);
    }

    @Test
    void should_deposit_amount_with_initial_solde_not_empty() {
        // Given
        BigDecimal depot1 = BigDecimal.valueOf(100);
        BigDecimal depot2 = BigDecimal.valueOf(120);
        UUID id = bankService.createAccount("Ali").getId();
        bankService.deposit(depot1,id);

        //when
        bankService.deposit(depot2,id);

        // then
        Assertions.assertThat(bankService.getAccountById(id).getSolde()).isEqualTo(depot1.add(depot2));
    }
    @Test
    void should_throw_exception_when_deposit_with_unknown_account() {
        Assertions.assertThatThrownBy(() -> bankService.deposit(BigDecimal.valueOf(50),UUID.randomUUID())).isInstanceOf(AccountNotFoundException.class);
    }

    @Test
    void should_throw_exception_when_deposit_with_negative_amount() {
        UUID id = bankService.createAccount("Ali").getId();
        Assertions.assertThatThrownBy(() -> bankService.deposit(BigDecimal.valueOf(-50),id)).isInstanceOf(AnauthorizedAmountException.class);
    }

    @Test
    void should_withdraw_amount() {
        // Given
        BigDecimal depot = BigDecimal.valueOf(100);
        BigDecimal withdraw = BigDecimal.valueOf(100);
        UUID id = bankService.createAccount("Ali").getId();
        bankService.deposit(depot,id);

        //when
        bankService.withdraw(withdraw,id);

        // then
        Assertions.assertThat(bankService.getAccountById(id).getSolde()).isEqualTo(depot.min(withdraw));
    }

    @Test
    void should_throw_exception_when_withdraw_with_negative_amount() {
        UUID id = bankService.createAccount("Ali").getId();
        Assertions.assertThatThrownBy(() -> bankService.withdraw(BigDecimal.valueOf(-50),id)).isInstanceOf(AnauthorizedAmountException.class);
    }
    @Test
    void should_throw_exception_when_withdraw_with_bank_overdraft_authorized_exceeded() {
        UUID id = bankService.createAccount("Ali").getId();
        Assertions.assertThatThrownBy(() -> bankService.withdraw(BigDecimal.valueOf(10),id)).isInstanceOf(BankOverdraftAuthorizedExceededException.class);
    }
}
