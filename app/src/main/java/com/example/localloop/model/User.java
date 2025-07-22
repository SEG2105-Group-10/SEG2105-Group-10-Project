package com.example.localloop.model;

public class User {
    private String username;
    private String firstName;
    private String role;
    private boolean disabled;  // <-- Add this

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isDisabled() { return disabled; } // Getter
    public void setDisabled(boolean disabled) { this.disabled = disabled; } // Setter
}
