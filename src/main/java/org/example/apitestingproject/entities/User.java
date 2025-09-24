package org.example.apitestingproject.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "USERS")
public class User {

    public enum YesNo { Yes, No }
    public enum AccountStatus { Pending, Activated, Rejected }
    public enum Role {User, Super_Admin, Sub_Admin}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private int id;

    @Column(name = "NAME", length = 100, nullable = false)
    private String name;

    @Column(name = "DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

    @Column(name = "PHONE_NO", length = 15, nullable = false, unique = true)
    private String phoneNo;

    @Column(name = "EMAIL", length = 150, nullable = false, unique = true)
    private String email;

    @Column(name = "USERNAME", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @Column(name = "PASSWORDUPDATE_TIMESTAMP")
    private LocalDateTime passwordUpdateTimestamp;

    @Column(name = "ADDRESS", length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE",length = 20,nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "CARD_TYPE_ID", foreignKey = @ForeignKey(name = "FK_USERS_CARDTYPE"))
    private CardType preferredCardType;

    @ManyToOne
    @JoinColumn(name = "BANK_ID", foreignKey = @ForeignKey(name = "FK_USERS_BANK"))
    private Bank bank;

    @Column(name = "SAVINGS_ACCOUNT_NO", length = 30)
    private String savingsAccountNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "JOINING_FEE_PAID", length = 3)
    private YesNo joiningFeePaid;


    @Enumerated(EnumType.STRING)
    @Column(name = "ACCOUNT_STATUS", length = 15)
    private AccountStatus accountStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DocumentVerification> documents = new ArrayList<>();

    // convenience methods to sync both sides
    public void addDocument(DocumentVerification doc) {
        documents.add(doc);
        doc.setUser(this);
    }

    public void removeDocument(DocumentVerification doc) {
        documents.remove(doc);
        doc.setUser(null);
    }

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getPasswordUpdateTimestamp() { return passwordUpdateTimestamp; }
    public void setPasswordUpdateTimestamp(LocalDateTime ts) { this.passwordUpdateTimestamp = ts; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public CardType getPreferredCardType() { return preferredCardType; }
    public void setPreferredCardType(CardType preferredCardType) { this.preferredCardType = preferredCardType; }

    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }

    public String getSavingsAccountNo() { return savingsAccountNo; }
    public void setSavingsAccountNo(String savingsAccountNo) { this.savingsAccountNo = savingsAccountNo; }

    public YesNo getJoiningFeePaid() { return joiningFeePaid; }
    public void setJoiningFeePaid(YesNo joiningFeePaid) { this.joiningFeePaid = joiningFeePaid; }

    public AccountStatus getAccountStatus() { return accountStatus; }
    public void setAccountStatus(AccountStatus accountStatus) { this.accountStatus = accountStatus; }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<DocumentVerification> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentVerification> documents) {
        this.documents = documents;
    }

    public User(String name, LocalDate dateOfBirth, String phoneNo, String email, String username, String password, LocalDateTime passwordUpdateTimestamp, String address, Role role, CardType preferredCardType, Bank bank, String savingsAccountNo, YesNo joiningFeePaid, AccountStatus accountStatus, List<DocumentVerification> documents) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.phoneNo = phoneNo;
        this.email = email;
        this.username = username;
        this.password = password;
        this.passwordUpdateTimestamp = passwordUpdateTimestamp;
        this.address = address;
        this.role = role;
        this.preferredCardType = preferredCardType;
        this.bank = bank;
        this.savingsAccountNo = savingsAccountNo;
        this.joiningFeePaid = joiningFeePaid;
        this.accountStatus = accountStatus;
        this.documents = documents;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", passwordUpdateTimestamp=" + passwordUpdateTimestamp +
                ", address='" + address + '\'' +
                ", role=" + role +
                ", preferredCardType=" + preferredCardType +
                ", bank=" + bank +
                ", savingsAccountNo='" + savingsAccountNo + '\'' +
                ", joiningFeePaid=" + joiningFeePaid +
                ", accountStatus=" + accountStatus +
                ", documents=" + documents +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof User u)) return false; return Objects.equals(id,u.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

