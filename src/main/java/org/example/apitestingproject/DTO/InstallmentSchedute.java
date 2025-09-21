package org.example.apitestingproject.DTO;

import org.example.apitestingproject.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentSchedute(
        int scheduleId,
        LocalDate dueDate,
        BigDecimal installmentAmount,
        int installmentNo,
        String paymentStatus,
        Transaction paidTxnId,
        int purchaseId
) {}
