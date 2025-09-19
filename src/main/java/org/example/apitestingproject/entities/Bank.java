package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BANKS")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BANK_ID")
    private int id;

    @Column(name = "BANK_NAME", length = 100, nullable = false, unique = true)
    private String bankName;

    @Column(name = "IFSC_CODE_PREFIX", length = 10, nullable = false, unique = true)
    private String ifscCodePrefix;

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getIfscCodePrefix() { return ifscCodePrefix; }
    public void setIfscCodePrefix(String ifscCodePrefix) { this.ifscCodePrefix = ifscCodePrefix; }

    public Bank() {
    }

    public Bank(String bankName, String ifscCodePrefix) {
        this.bankName = bankName;
        this.ifscCodePrefix = ifscCodePrefix;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", bankName='" + bankName + '\'' +
                ", ifscCodePrefix='" + ifscCodePrefix + '\'' +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Bank b)) return false; return Objects.equals(id,b.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

