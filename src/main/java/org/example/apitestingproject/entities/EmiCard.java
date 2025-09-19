package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "EMI_CARDS")
public class EmiCard {

    public enum ActivationStatus { Active, Inactive }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_ID")
    private int id;

    @OneToOne (optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_EMICARDS_USER"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "CARD_TYPE_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_EMICARDS_CARDTYPE"))
    private CardType cardType;

    @Column(name = "CARD_NUMBER", length = 20, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "VALID_TILL", nullable = false)
    private LocalDate validTill;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTIVATION_STATUS", length = 10,nullable = false)
    private ActivationStatus activationStatus;

    @Column(name = "CREDIT_LIMIT", precision = 12, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "AVAILING_LIMIT", precision = 12, scale = 2)
    private BigDecimal availableLimit;


    public EmiCard() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CardType getCardType() {
        return cardType;
    }



    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getValidTill() {
        return validTill;
    }

    public void setValidTill(LocalDate validTill) {
        this.validTill = validTill;
    }

    public ActivationStatus getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(ActivationStatus activationStatus) {
        this.activationStatus = activationStatus;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getAvailableLimit() {
        return availableLimit;
    }

    public void setAvailableLimit(BigDecimal availableLimit) {
        this.availableLimit = availableLimit;
    }


    public void setCardType(CardType cardType) {
        this.cardType = cardType;
        if (cardType != null) {
            if (cardType.getId() == 1) {
                this.creditLimit = BigDecimal.valueOf(300000);
            } else if (cardType.getId() == 2) {
                this.creditLimit = BigDecimal.valueOf(500000);
            }
        }
    }

    @PrePersist @PreUpdate
    private void ensureCreditLimit() {
        if (cardType != null && creditLimit == null) {
            if (cardType.getId() == 1) {
                this.creditLimit = BigDecimal.valueOf(300000);
            } else if (cardType.getId() == 2) {
                this.creditLimit = BigDecimal.valueOf(500000);
            }
        }
    }


    public EmiCard(User user, CardType cardType, String cardNumber, LocalDate validTill, ActivationStatus activationStatus, BigDecimal creditLimit, BigDecimal availableLimit) {
        this.user = user;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.validTill = validTill;
        this.activationStatus = activationStatus;
        this.creditLimit = creditLimit;
        this.availableLimit = availableLimit;
    }

    @Override
    public String toString() {
        return "EmiCard{" +
                "id=" + id +
                ", user=" + user +
                ", cardType=" + cardType +
                ", cardNumber='" + cardNumber + '\'' +
                ", validTill=" + validTill +
                ", activationStatus=" + activationStatus +
                ", creditLimit=" + creditLimit +
                ", availableLimit=" + availableLimit +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof EmiCard c)) return false; return Objects.equals(id,c.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}