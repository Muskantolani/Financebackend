package org.example.apitestingproject.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Purchase purchase;

    @Column(name = "TRANSACTION_DATE")
    private LocalDate transactionDate;

    @Column(name = "AMOUNT_PAID", precision = 12, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    // Added Transaction method

    public String getTransaction_method() {
        return transaction_method;
    }

    public void setTransaction_method(String transaction_method) {
        this.transaction_method = transaction_method;
    }

    @Column(name = "TRANSACTION_METHOD", nullable = true)
    private  String transaction_method;


    public enum TransactionType {
        PROCESSING_FEE,
        INSTALLMENT
    }




    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionType transactionType;

    // getters/setters, equals/hashCode

    public Transaction(Purchase purchase, LocalDate transactionDate, BigDecimal amountPaid) {
        this.purchase = purchase;
        this.transactionDate = transactionDate;
        this.amountPaid = amountPaid;
    }


    public Transaction(int id, Purchase purchase, LocalDate transactionDate, BigDecimal amountPaid, String transaction_method, TransactionType transactionType) {
        this.id = id;
        this.purchase = purchase;
        this.transactionDate = transactionDate;
        this.amountPaid = amountPaid;
        this.transaction_method = transaction_method;
        this.transactionType = transactionType;
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

