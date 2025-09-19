package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "APPROVAL_STATUS",
        uniqueConstraints = @UniqueConstraint(name = "UK_APPROVAL_USER", columnNames = {"USER_ID"})
)
public class ApprovalStatus {

    public enum Stage { Document_Check, Final_Activation, Card_Dispatch }
    public enum Decision { Pending, Approved, Rejected }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPROVAL_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_APPROVAL_USER"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "APPROVED_BY", nullable = true, foreignKey = @ForeignKey(name = "FK_APPROVAL_ADMIN"))
    private Admin approvedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPROVAL_STAGE", length = 30)
    private Stage approvalStage;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10)
    private Decision status;

    @Column(name = "REMARKS", length = 255)
    private String remarks;

    @Column(name = "APPROVAL_DATE")
    private LocalDate approvalDate;

    public ApprovalStatus() {
    }

    public ApprovalStatus( User user, Admin approvedBy, Stage approvalStage, Decision status, String remarks, LocalDate approvalDate) {
        this.user = user;
        this.approvedBy = approvedBy;
        this.approvalStage = approvalStage;
        this.status = status;
        this.remarks = remarks;
        this.approvalDate = approvalDate;
    }


    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Admin getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Admin approvedBy) { this.approvedBy = approvedBy; }

    public Stage getApprovalStage() { return approvalStage; }
    public void setApprovalStage(Stage approvalStage) { this.approvalStage = approvalStage; }

    public Decision getStatus() { return status; }
    public void setStatus(Decision status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDate approvalDate) { this.approvalDate = approvalDate; }

    @Override
    public String toString() {
        return "ApprovalStatus{" +
                "id=" + id +
                ", user=" + user +
                ", approvedBy=" + approvedBy +
                ", approvalStage=" + approvalStage +
                ", status=" + status +
                ", remarks='" + remarks + '\'' +
                ", approvalDate=" + approvalDate +
                '}';
    }


    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof ApprovalStatus s)) return false; return Objects.equals(id,s.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

