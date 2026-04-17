package com.kata.bankapi.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

    public void logTransfer(String sourceAccountId, String destinationAccountId, String amount, String currency) {
        LOGGER.info("AUDIT TRANSFER_CREATED sourceAccountId={} destinationAccountId={} amount={} currency={}",
                sourceAccountId, destinationAccountId, amount, currency);
    }
}
