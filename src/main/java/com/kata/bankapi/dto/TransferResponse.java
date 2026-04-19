package com.kata.bankapi.dto;

import java.util.UUID;

public record TransferResponse(UUID transferId, String status) {
}
