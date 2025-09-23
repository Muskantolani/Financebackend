package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "AUDIT_LOG",
        indexes = {
                @Index(name = "IDX_AUDIT_USER", columnList = "USER_ID"),
                @Index(name = "IDX_AUDIT_ADMIN", columnList = "ADMIN_ID"),
                @Index(name = "IDX_AUDIT_TIMESTAMP", columnList = "ACTION_TIMESTAMP")
        }
)
public class AuditLog {

    public enum ActionType { Login, Update_Profile, Payment, Approval, Deactivation, Registration }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_AUDIT_USER"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "ADMIN_ID", foreignKey = @ForeignKey(name = "FK_AUDIT_ADMIN"))
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "ACTION_TYPE", length = 30,nullable = false)
    private ActionType actionType;

    @Column(name = "ACTION_TIMESTAMP",nullable = false)
    private LocalDateTime actionTimestamp;

    @Column(name = "IP_ADDRESS", length = 50)
    private String ipAddress;

    public AuditLog() {
    }

    public AuditLog(User user, Admin admin, ActionType actionType, LocalDateTime actionTimestamp, String ipAddress) {
        this.user = user;
        this.admin = admin;
        this.actionType = actionType;
        this.actionTimestamp = actionTimestamp;
        this.ipAddress = ipAddress;
    }

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public LocalDateTime getActionTimestamp() { return actionTimestamp; }
    public void setActionTimestamp(LocalDateTime actionTimestamp) { this.actionTimestamp = actionTimestamp; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", user=" + user +
                ", admin=" + admin +
                ", actionType=" + actionType +
                ", actionTimestamp=" + actionTimestamp +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof AuditLog a)) return false; return Objects.equals(id,a.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

