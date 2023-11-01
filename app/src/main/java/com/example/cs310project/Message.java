package com.example.cs310project;

public class Message {
    private String text;

    public Message() {
        // Required for Firebase
    }

    public Message(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

