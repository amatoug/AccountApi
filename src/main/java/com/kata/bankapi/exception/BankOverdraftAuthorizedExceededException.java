package com.kata.bankapi.exception;

public class BankOverdraftAuthorizedExceededException extends RuntimeException {
    public BankOverdraftAuthorizedExceededException(String message) {
        super(message);
    }
}
