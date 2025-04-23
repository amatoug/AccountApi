package com.kata.bankapi.modele;

import java.math.BigDecimal;
import java.util.UUID;

public class BankAccount {
    UUID id;
    String name;
    BigDecimal solde;

    public BankAccount(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
        this.solde = BigDecimal.ZERO;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }
}
