package org.example.apitestingproject.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "NOTIFICATIONS",
        indexes = {
                @Index(name = "IDX_NOTIFY_USER_STATUS", columnList = "USER_ID, STATUS")
        }
)
public class Notification {

    public enum Status { Read, Unread }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTIFY_USER"))
    private User user;

    @Column(name = "MESSAGE", length = 255, nullable = false)
    private String message;

    @Column(name = "NOTIFICATION_DATE")
    private LocalDateTime notificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 10)
    private Status status;

    // getters/setters, equals/hashCode
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getNotificationDate() { return notificationDate; }
    public void setNotificationDate(LocalDateTime notificationDate) { this.notificationDate = notificationDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Notification n)) return false; return Objects.equals(id,n.id); }
    @Override public int hashCode(){ return Objects.hashCode(id); }
}

