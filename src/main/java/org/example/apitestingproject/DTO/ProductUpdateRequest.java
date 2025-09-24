package org.example.apitestingproject.dto;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ProductUpdateRequest {

    @NotBlank
    @Size(max = 100)
    private String productName;

    @Size(max = 255)
    private String productDetails;

    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal cost;

    @NotNull
    @Min(0)
    private Integer productStock;

    @NotBlank
    private String category;

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductDetails() { return productDetails; }
    public void setProductDetails(String productDetails) { this.productDetails = productDetails; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public Integer getProductStock() { return productStock; }
    public void setProductStock(Integer productStock) { this.productStock = productStock; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}

