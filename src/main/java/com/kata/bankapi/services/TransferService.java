package com.kata.bankapi.services;

import com.kata.bankapi.dto.TransferRequest;
import com.kata.bankapi.dto.TransferResponse;
import com.kata.bankapi.exception.InsufficientFundsException;
import com.kata.bankapi.exception.InvalidTransferAmountException;
import com.kata.bankapi.exception.InvalidTransferRequestException;
import com.kata.bankapi.modele.BankAccount;
import com.kata.bankapi.modele.TransferEntity;
import com.kata.bankapi.modele.TransferStatus;
import com.kata.bankapi.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final BankService bankService;
    private final AuditService auditService;

    public TransferService(TransferRepository transferRepository, BankService bankService, AuditService auditService) {
        this.transferRepository = transferRepository;
        this.bankService = bankService;
        this.auditService = auditService;
    }

    @Transactional
    public TransferResponse createTransfer(TransferRequest request) {
        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Transfer amount must be greater than zero");
        }
        if (request.sourceAccountId().equals(request.destinationAccountId())) {
            throw new InvalidTransferRequestException("sourceAccountId and destinationAccountId must be different");
        }

        BankAccount sourceAccount = bankService.getAccountById(request.sourceAccountId());
        bankService.getAccountById(request.destinationAccountId());

        if (sourceAccount.getSolde().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds on source account");
        }

        bankService.withdraw(request.amount(), request.sourceAccountId());
        bankService.deposit(request.amount(), request.destinationAccountId());

        TransferEntity entity = new TransferEntity();
        entity.setSourceAccountId(request.sourceAccountId());
        entity.setDestinationAccountId(request.destinationAccountId());
        entity.setAmount(request.amount());
        entity.setCurrency(request.currency());
        entity.setReference(request.reference());
        entity.setStatus(TransferStatus.COMPLETED);
        TransferEntity saved = transferRepository.save(entity);

        auditService.logTransfer(
                request.sourceAccountId().toString(),
                request.destinationAccountId().toString(),
                request.amount().toPlainString(),
                request.currency()
        );

        return new TransferResponse(saved.getId(), saved.getStatus().name());
    }
}
