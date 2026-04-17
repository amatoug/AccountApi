package com.kata.bankapi.controller;

import com.kata.bankapi.dto.TransferRequest;
import com.kata.bankapi.dto.TransferResponse;
import com.kata.bankapi.services.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('TRANSFER_CREATE')")
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse createTransfer(@Valid @RequestBody TransferRequest request) {
        return transferService.createTransfer(request);
    }
}
