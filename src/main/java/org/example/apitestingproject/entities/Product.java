package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "PRODUCTS",
        indexes = @Index(name = "IDX_PRODUCT_NAME", columnList = "PRODUCT_NAME")
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private int id;

    @Column(name = "PRODUCT_NAME", length = 100,nullable = false)
    private String productName;

    @Column(name = "PRODUCT_DETAILS", length = 255)
    private String productDetails;

    @Column(name = "PRODUCT_PRICE", precision = 12, scale = 2,nullable = false)
    private BigDecimal cost;

    @Column(name = "PRODUCT_STOCK",nullable = false)
    private Integer productStock=0;

    @Column(name= "CATEGORY", nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "CREATED_BY", foreignKey = @ForeignKey(name = "FK_PRODUCTS_ADMIN"))
    private Admin createdBy;

    // getters/setters, equals/hashCode
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

    public Admin getCreatedBy() { return createdBy; }
    public void setCreatedBy(Admin createdBy) { this.createdBy = createdBy; }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Product() {
    }

    public Product(String productName, String productDetails, BigDecimal cost, Integer productStock, String category, Admin createdBy) {
        this.productName = productName;
        this.productDetails = productDetails;
        this.cost = cost;
        this.productStock = productStock;
        this.category = category;
        this.createdBy = createdBy;
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Product p)) return false; return Objects.equals(id,p.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

