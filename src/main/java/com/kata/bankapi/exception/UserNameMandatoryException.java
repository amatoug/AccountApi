package com.kata.bankapi.exception;

public class UserNameMandatoryException extends RuntimeException {
    public UserNameMandatoryException(String message) {
        super(message);
    }
}
