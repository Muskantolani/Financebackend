package org.example.apitestingproject.DTO;

public class PurchaseRequestDTO {
    private Long userId;
    private Long productId;
    private int tenureMonths;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public int getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(int tenureMonths) { this.tenureMonths = tenureMonths; }
}
