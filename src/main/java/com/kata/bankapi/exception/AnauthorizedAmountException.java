package com.kata.bankapi.exception;

public class AnauthorizedAmountException extends RuntimeException {
    public AnauthorizedAmountException(String message) {
        super(message);
    }
}
