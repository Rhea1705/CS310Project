package com.example.cs310project;

public class Message {
    private String text;
    private String sender;
    private String receiver;

    public Message() {
        // Required for Firebase
    }

    public Message(String text, String sender, String receiver) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getSender() {
        return sender;
    }
    public String getReceiver() {
        return receiver;
    }
}

