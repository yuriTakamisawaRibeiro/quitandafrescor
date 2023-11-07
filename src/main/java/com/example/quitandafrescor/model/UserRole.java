package com.example.quitandafrescor.model;

public enum UserRole {
    ADMIN("admin"),
    CLIENT("client");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
