package com.kata.bankapi.modele;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class BankAccount {
    UUID id;
    String name;
    BigDecimal solde;

    public BankAccount(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.solde = BigDecimal.ZERO;
    }
}
