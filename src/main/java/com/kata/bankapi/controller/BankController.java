package com.kata.bankapi.controller;

import com.kata.bankapi.modele.BankAccount;
import com.kata.bankapi.services.BankService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankAccount createAccount(@RequestBody Map<String, String> body) {
        return bankService.createAccount(body.get("name"));
    }

    @GetMapping("/{id}")
    public BankAccount getAccount(@PathVariable UUID id) {
        return bankService.getAccountById(id);
    }

    @PostMapping("/{id}/deposit")
    public BankAccount deposit(@PathVariable UUID id, @RequestBody Map<String, BigDecimal> body) {
        bankService.deposit(body.get("amount"), id);
        return bankService.getAccountById(id);
    }

    @PostMapping("/{id}/withdraw")
    public BankAccount withdraw(@PathVariable UUID id, @RequestBody Map<String, BigDecimal> body) {
        bankService.withdraw(body.get("amount"), id);
        return bankService.getAccountById(id);
    }
}
