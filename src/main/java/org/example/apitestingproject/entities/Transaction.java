package org.example.apitestingproject.entities;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PURCHASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_TXN_PURCHASE"))
    private Purchase purchase;

    @Column(name = "TRANSACTION_DATE")
    private LocalDate transactionDate;

    @Column(name = "AMOUNT_PAID", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    // getters/setters, equals/hashCode

    public Transaction(Purchase purchase, LocalDate transactionDate, BigDecimal amountPaid) {
        this.purchase = purchase;
        this.transactionDate = transactionDate;
        this.amountPaid = amountPaid;
    }

    public Transaction() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public LocalDate getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDate transactionDate) { this.transactionDate = transactionDate; }

    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Transaction t)) return false; return Objects.equals(id,t.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

