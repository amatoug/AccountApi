package com.kata.bankapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferRequest(
        @NotNull(message = "sourceAccountId is required")
        UUID sourceAccountId,
        @NotNull(message = "destinationAccountId is required")
        UUID destinationAccountId,
        @NotNull(message = "amount is required")
        @DecimalMin(value = "0.01", message = "amount must be greater than zero")
        BigDecimal amount,
        @NotBlank(message = "currency is required")
        @Pattern(regexp = "^[A-Z]{3}$", message = "currency must be a 3-letter uppercase code")
        String currency,
        @Size(max = 255, message = "reference max length is 255")
        String reference
) {
}
