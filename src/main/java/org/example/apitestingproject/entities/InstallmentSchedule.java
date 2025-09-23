package org.example.apitestingproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "INSTALLMENT_SCHEDULE")
public class InstallmentSchedule {

    public enum PaymentStatus { Paid, Pending, Overdue }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "PURCHASE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SCHEDULE_PURCHASE"))
    @JsonBackReference
    private Purchase purchase;

    @Column(name = "INSTALLMENT_NO", nullable = false)
    private Integer installmentNo;

    @Column(name = "DUE_DATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "INSTALLMENT_AMOUNT", precision = 12, scale = 2, nullable = false)
    private BigDecimal installmentAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_STATUS", length = 10)
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "PAID_TXN_ID", foreignKey = @ForeignKey(name = "FK_SCHEDULE_TXN"))
    private Transaction paidTransaction;

    @Column(name = "PENALTY_AMOUNT", precision = 12, scale = 2, nullable = true)
    private BigDecimal penaltyAmount = BigDecimal.ZERO;
    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(BigDecimal penaltyAmount) { this.penaltyAmount = penaltyAmount; }

    // getters/setters, equals/hashCode


    public InstallmentSchedule(Purchase purchase, Integer installmentNo, LocalDate dueDate, BigDecimal installmentAmount, PaymentStatus paymentStatus, Transaction paidTransaction) {
        this.purchase = purchase;
        this.installmentNo = installmentNo;
        this.dueDate = dueDate;
        this.installmentAmount = installmentAmount;
        this.paymentStatus = paymentStatus;
        this.paidTransaction = paidTransaction;
    }


    public Transaction getPaidTransaction() {
        return paidTransaction;
    }

    public void setPaidTransaction(Transaction paidTransaction) {
        this.paidTransaction = paidTransaction;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Purchase getPurchase() { return purchase; }
    public void setPurchase(Purchase purchase) { this.purchase = purchase; }

    public Integer getInstallmentNo() { return installmentNo; }
    public void setInstallmentNo(Integer installmentNo) { this.installmentNo = installmentNo; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getInstallmentAmount() { return installmentAmount; }
    public void setInstallmentAmount(BigDecimal installmentAmount) { this.installmentAmount = installmentAmount; }

    public PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }

    public InstallmentSchedule() {
    }

    public InstallmentSchedule(Purchase purchase, Integer installmentNo, LocalDate dueDate, BigDecimal installmentAmount, PaymentStatus paymentStatus) {
        this.purchase = purchase;
        this.installmentNo = installmentNo;
        this.dueDate = dueDate;
        this.installmentAmount = installmentAmount;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "InstallmentSchedule{" +
                "id=" + id +
                ", purchase=" + purchase +
                ", installmentNo=" + installmentNo +
                ", dueDate=" + dueDate +
                ", installmentAmount=" + installmentAmount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof InstallmentSchedule s)) return false; return Objects.equals(id,s.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

