package com.example.cs310project;

public class UserData {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String imageUrl;
    private String role;
    private String USCid;

    public UserData() {
        // Default constructor required for DataSnapshot.getValue(UserData.class)
    }

    public UserData(String username, String password, String email, String phoneNumber, String imageUrl, String role, String USCid) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.USCid = USCid;
    }

    // Add getter and setter methods
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUSCid() {
        return USCid;
    }

    public void setUSCid(String USCid) {
        this.USCid = USCid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
