package org.example.apitestingproject.dto;

public class AdminDto {
    private int id;
    private String username;
    private String role;

    public AdminDto(int id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
