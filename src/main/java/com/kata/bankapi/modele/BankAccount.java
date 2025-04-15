package com.kata.bankapi.modele;

import java.util.UUID;

public class BankAccount {
    UUID id;
    String name;

    public BankAccount(String name) {
        this.name = name;
        this.id = UUID.randomUUID();
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
}
