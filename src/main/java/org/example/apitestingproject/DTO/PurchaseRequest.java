package org.example.apitestingproject.DTO;

import java.math.BigDecimal;
import java.util.List;

public class PurchaseRequest {
    private int userId;
    private int cardId;
    private int tenurePeriod;
    private BigDecimal processingFee;

    public List<PurchaseItemRequest> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItemRequest> items) {
        this.items = items;
    }

    public BigDecimal getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(BigDecimal processingFee) {
        this.processingFee = processingFee;
    }

    public int getTenurePeriod() {
        return tenurePeriod;
    }

    public void setTenurePeriod(int tenurePeriod) {
        this.tenurePeriod = tenurePeriod;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private List<PurchaseItemRequest> items;


}
