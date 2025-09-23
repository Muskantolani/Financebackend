package org.example.apitestingproject.DTO;

import org.example.apitestingproject.entities.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InstallmentScheduleDTO(
        int scheduleId,
        LocalDate dueDate,
        BigDecimal installmentAmount,
        int installmentNo,
        String paymentStatus,
        Transaction paidTxnId,
        int purchaseId,
        List<String> productNames,
        BigDecimal penaltyAmount
) {}
