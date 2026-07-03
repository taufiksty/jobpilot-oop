package model;

import java.time.LocalDateTime;

public class User extends BaseModel {
    private String name;
    private String password;
    private LocalDateTime lastLoginAt;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    public User(String id, String name, String password) {
        super(id);
        this.name = name;
        this.password = password;
    }

    public String toString() {
        return "User{id=" + getId() + ", name='" + name + "', lastLoginAt=" + lastLoginAt + "}";
    }

    // Getters and Setters for User-specific fields
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
}
