package org.example.apitestingproject.entities;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PURCHASE_ITEMS")
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_ITEM_ID")
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PURCHASE_ITEM_PURCHASE"))
    private Purchase purchase;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false,
            foreignKey = @ForeignKey(name = "FK_PURCHASE_ITEM_PRODUCT"))
    private Product product;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "UNIT_PRICE", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "LINE_TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal lineTotal;

    public PurchaseItem() {}
    public PurchaseItem(Purchase purchase, Product product, int qty, BigDecimal unitPrice){
        this.purchase = purchase;
        this.product = product;
        this.quantity = qty;
        this.unitPrice = unitPrice;
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));
    }


    @PrePersist @PreUpdate
    private void calc() {
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(BigDecimal lineTotal) {
        this.lineTotal = lineTotal;
    }

    // getters/setters/equals/hashCode…
}
