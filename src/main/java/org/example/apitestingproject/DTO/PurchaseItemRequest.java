package org.example.apitestingproject.DTO;

import java.math.BigDecimal;

public class PurchaseItemRequest {
    private int productId;
    private int quantity;
    private BigDecimal unitPrice; // can also be derived from Product, but good to send

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
// getters and setters
}