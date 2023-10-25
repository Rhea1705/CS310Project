package com.example.cs310project;

public class User {
    String name;
    String email;
    String password;
    String role;
    String usc_id;

    public User() {
    }

    public User(String name, String email, String password, String role, String usc_id) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.usc_id = usc_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsc_id() {
        return usc_id;
    }

    public void setUsc_id(String usc_id) {
        this.usc_id = usc_id;
    }
}
