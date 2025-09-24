package org.example.apitestingproject.dto;
import java.math.BigDecimal;

public class ProductAdminDisplayDTO {
    private int id;
    private String productName;
    private String productDetails;
    private BigDecimal cost;
    private Integer productStock;
    private String category;



    public ProductAdminDisplayDTO() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

