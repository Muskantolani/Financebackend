package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "CARD_TYPES")
public class CardType {

    public enum Name { GOLD, TITANIUM }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARD_TYPE_ID")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CARD_TYPE_NAME", length = 50, nullable = false,unique = true)
    private Name name;

    @Column(name = "DEFAULT_LIMIT", precision = 12, scale = 2)
    private BigDecimal defaultLimit;

    @Column(name = "JOINING_FEE", precision = 12, scale = 2)
    private BigDecimal joiningFee;

    public CardType() {
    }

    public CardType( Name name, BigDecimal defaultLimit, BigDecimal joiningFee) {
        this.name = name;
        this.defaultLimit = defaultLimit;
        this.joiningFee = joiningFee;
    }

    @Override
    public String toString() {
        return "CardType{" +
                "id=" + id +
                ", name=" + name +
                ", defaultLimit=" + defaultLimit +
                ", joiningFee=" + joiningFee +
                '}';
    }

    // getters/setters, equals/hashCode

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Name getName() { return name; }
    public void setName(Name name) { this.name = name; }

    public BigDecimal getDefaultLimit() { return defaultLimit; }
    public void setDefaultLimit(BigDecimal defaultLimit) { this.defaultLimit = defaultLimit; }

    public BigDecimal getJoiningFee() { return joiningFee; }
    public void setJoiningFee(BigDecimal joiningFee) { this.joiningFee = joiningFee; }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof CardType ct)) return false; return Objects.equals(id,ct.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

