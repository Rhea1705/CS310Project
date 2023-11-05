package com.example.cs310project;

public class User {
    String name;
    String email;
    String password;
    String phone_number;
    String usc_id;
    String role;

    String username;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    String imageUrl;
    String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User() {
    }

    public User(String name, String email, String password, String phone_number,String usc_id,String role, String username,String uid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.usc_id = usc_id;
        this.role = role;
        this.username = username;
        this.uid = uid;


    }

    public String getUsc_id() {
        return usc_id;
    }

    public void setUsc_id(String usc_id) {
        this.usc_id = usc_id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


}
