package com.kata.bankapi.dto;

public record ApiError(String errorCode, String message, String requestId) {
}
