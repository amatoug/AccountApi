package com.kata.bankapi.exception;

public class InvalidTransferRequestException extends RuntimeException {
    public InvalidTransferRequestException(String message) {
        super(message);
    }
}
