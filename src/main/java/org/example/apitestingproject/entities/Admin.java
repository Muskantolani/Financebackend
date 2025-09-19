package org.example.apitestingproject.entities;


import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ADMINS")
public class Admin {

    public enum Role { SUPER_ADMIN, SUB_ADMIN }

    @Id
    @Column(name = "ADMIN_ID")
    private int id;

    @Column(name = "USERNAME", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 20, nullable = false)
    private Role role;

    public Admin() {
    }

    // getters/setters, equals/hashCode by id

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Admin a))
            return false;
        return Objects.equals(id,a.id);
    }
    @Override public int hashCode(){
        return Objects.hashCode(id);
    }


    public Admin(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }


    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}

