package com.example.cs310project;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class MessageTest {

    @Test
    public void testMessageConstructor() {
        String text = "Hello!";
        String sender = "Alice";
        String receiver = "Bob";

        Message message = new Message(text, sender, receiver);

        assertEquals(text, message.getText());
        assertEquals(sender, message.getSender());
        assertEquals(receiver, message.getReceiver());
    }

    @Test
    public void testSetText() {
        Message message = new Message();
        String newText = "How are you?";

        message.setText(newText);

        assertEquals(newText, message.getText());
    }
}
