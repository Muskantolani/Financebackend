package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "PURCHASES")
public class Purchase {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PURCHASE_USER"))
    private User user;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();

    @PrePersist @PreUpdate
    private void recomputeAmount() {
        this.amount = items.stream()
                .map(PurchaseItem::getLineTotal)
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
    }

    public void addItem(Product p, int qty, BigDecimal unitPrice) {
        PurchaseItem item = new PurchaseItem(this, p, qty, unitPrice);
        items.add(item);
    }
    public void removeItem(PurchaseItem item) {
        items.remove(item);
        item.setPurchase(null);
    }

//    BigDecimal grandTotal = amount+processingFeeApplied;



    @Column(name = "PROCESSING_FEE_APPLIED", precision = 12, scale = 2,nullable = false)
    private BigDecimal processingFeeApplied=BigDecimal.ZERO.setScale(2);

    @ManyToOne(optional = false)
    @JoinColumn(name = "CARD_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PURCHASE_CARD"))
    private EmiCard card;

    @Column(name = "PURCHASE_DATE",nullable = false)
    private LocalDate purchaseDate;

    @Column(name = "AMOUNT", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "TENURE_PERIOD")
    private Integer tenurePeriod; // 3,6,9,12


    @Transient
    public BigDecimal getGrandTotal() { return amount.add(processingFeeApplied); }
    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Product> getProducts() {
        return items.stream().map(PurchaseItem::getProduct).toList();
    }

    public EmiCard getCard() { return card; }
    public void setCard(EmiCard card) { this.card = card; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Integer getTenurePeriod() { return tenurePeriod; }
    public void setTenurePeriod(Integer tenurePeriod) { this.tenurePeriod = tenurePeriod; }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Purchase p)) return false; return Objects.equals(id,p.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }

    public BigDecimal getProcessingFeeApplied() {
        return processingFeeApplied;
    }

    public void setProcessingFeeApplied(BigDecimal processingFeeApplied) {
        this.processingFeeApplied = processingFeeApplied;
    }

    public Purchase() {
    }

    public Purchase(User user, List<PurchaseItem> items, BigDecimal processingFeeApplied, EmiCard card, LocalDate purchaseDate, BigDecimal amount, Integer tenurePeriod) {
        this.user = user;
        this.items = items;
        this.processingFeeApplied = processingFeeApplied;
        this.card = card;
        this.purchaseDate = purchaseDate;
        this.amount = amount;
        this.tenurePeriod = tenurePeriod;
    }

    public List<PurchaseItem> getItems() {
        return items;
    }

    public void setItems(List<PurchaseItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", user=" + user +
                ", items=" + items +
                ", processingFeeApplied=" + processingFeeApplied +
                ", card=" + card +
                ", purchaseDate=" + purchaseDate +
                ", amount=" + amount +
                ", tenurePeriod=" + tenurePeriod +
                '}';
    }
}


