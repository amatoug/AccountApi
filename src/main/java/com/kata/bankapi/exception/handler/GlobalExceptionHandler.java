package com.kata.bankapi.exception.handler;

import com.kata.bankapi.exception.AccountNotFoundException;
import com.kata.bankapi.exception.AnauthorizedAmountException;
import com.kata.bankapi.exception.BankOverdraftAuthorizedExceededException;
import com.kata.bankapi.exception.UserNameMandatoryException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({AnauthorizedAmountException.class, UserNameMandatoryException.class})
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BankOverdraftAuthorizedExceededException.class)
    public ResponseEntity<String> handleOverdraft(BankOverdraftAuthorizedExceededException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}
