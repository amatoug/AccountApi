package com.kata.bankapi.service;

import com.kata.bankapi.dto.TransferRequest;
import com.kata.bankapi.exception.InvalidTransferAmountException;
import com.kata.bankapi.exception.InvalidTransferRequestException;
import com.kata.bankapi.modele.BankAccount;
import com.kata.bankapi.modele.TransferEntity;
import com.kata.bankapi.modele.TransferStatus;
import com.kata.bankapi.repository.TransferRepository;
import com.kata.bankapi.services.AuditService;
import com.kata.bankapi.services.BankService;
import com.kata.bankapi.services.TransferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private BankService bankService;

    @Mock
    private AuditService auditService;

    private TransferService transferService;

    @BeforeEach
    void setUp() {
        transferService = new TransferService(transferRepository, bankService, auditService);
    }

    @Test
    void should_throw_when_amount_is_invalid() {
        TransferRequest request = new TransferRequest(
                UUID.randomUUID(),
                UUID.randomUUID(),
                BigDecimal.ZERO,
                "EUR",
                "ref"
        );

        assertThatThrownBy(() -> transferService.createTransfer(request))
                .isInstanceOf(InvalidTransferAmountException.class);
    }

    @Test
    void should_throw_when_source_and_destination_are_the_same() {
        UUID accountId = UUID.randomUUID();
        TransferRequest request = new TransferRequest(
                accountId,
                accountId,
                new BigDecimal("10.00"),
                "EUR",
                "ref"
        );

        assertThatThrownBy(() -> transferService.createTransfer(request))
                .isInstanceOf(InvalidTransferRequestException.class);
    }

    @Test
    void should_log_audit_entry_when_transfer_is_created() {
        UUID sourceId = UUID.randomUUID();
        UUID destinationId = UUID.randomUUID();
        TransferRequest request = new TransferRequest(
                sourceId,
                destinationId,
                new BigDecimal("10.00"),
                "EUR",
                "ref-1"
        );

        BankAccount sourceAccount = new BankAccount("source");
        sourceAccount.setId(sourceId);
        sourceAccount.setSolde(new BigDecimal("100.00"));
        BankAccount destinationAccount = new BankAccount("destination");
        destinationAccount.setId(destinationId);

        when(bankService.getAccountById(sourceId)).thenReturn(sourceAccount);
        when(bankService.getAccountById(destinationId)).thenReturn(destinationAccount);
        when(transferRepository.save(any(TransferEntity.class))).thenAnswer(invocation -> {
            TransferEntity saved = invocation.getArgument(0);
            saved.setStatus(TransferStatus.COMPLETED);
            return saved;
        });

        transferService.createTransfer(request);

        verify(auditService).logTransfer(sourceId.toString(), destinationId.toString(), "10.00", "EUR");
    }
}
