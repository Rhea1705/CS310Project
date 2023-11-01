package com.example.cs310project;

public class Message {
    private String text;
    private String email;

    public Message() {
        // Required for Firebase
    }

    public Message(String text, String email) {
        this.text = text;
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

