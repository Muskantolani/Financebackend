package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "OTP_VERIFICATION",
        indexes = {
                @Index(name = "IDX_OTP_USER_STATUS", columnList = "USER_ID, STATUS")
        }
)
public class OtpVerification {

    public enum Status { Verified, Expired, Pending}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OTP_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_OTP_USER"))
    private User user;

    @Column(name = "PHONE_NO", length = 15, nullable = false)
    private String phoneNo;

    @Column(name = "OTP_CODE", length = 10, nullable = false)
    private String otpCode;

    @Column(name = "OTP_TIMESTAMP",nullable = false)
    private LocalDateTime otpTimestamp;

    @Column(name = "EXPIRES_AT", nullable = false)
    private LocalDateTime expiresAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10,nullable = false)
    private Status status;

    public OtpVerification(User user, String phoneNo, String otpCode, LocalDateTime otpTimestamp, LocalDateTime expiresAt, Status status) {
        this.user = user;
        this.phoneNo = phoneNo;
        this.otpCode = otpCode;
        this.otpTimestamp = otpTimestamp;
        this.expiresAt = expiresAt;
        this.status = status;
    }

    public OtpVerification() {
    }

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getPhoneNo() { return phoneNo; }
    public void setPhoneNo(String phoneNo) { this.phoneNo = phoneNo; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public LocalDateTime getOtpTimestamp() { return otpTimestamp; }
    public void setOtpTimestamp(LocalDateTime otpTimestamp) { this.otpTimestamp = otpTimestamp; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return "OtpVerification{" +
                "id=" + id +
                ", user=" + user +
                ", phoneNo='" + phoneNo + '\'' +
                ", otpCode='" + otpCode + '\'' +
                ", otpTimestamp=" + otpTimestamp +
                ", expiresAt=" + expiresAt +
                ", status=" + status +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof OtpVerification v)) return false; return Objects.equals(id,v.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}
