package org.example.apitestingproject.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionResponse {
    private int transactionId;
    private int purchaseId;
    private BigDecimal amountPaid;
    private LocalDate transactionDate;
    private String status; // SUCCESS / FAILED

    public TransactionResponse(int transactionId, int purchaseId, BigDecimal amountPaid, LocalDate transactionDate, String status) {
        this.transactionId = transactionId;
        this.purchaseId = purchaseId;
        this.amountPaid = amountPaid;
        this.transactionDate = transactionDate;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

