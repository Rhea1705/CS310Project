package com.example.cs310project;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    @Test
    public void verify_email() {
        User user = new User();
        assertTrue(User.verify_email("Tester@usc.edu"));
        assertFalse(User.verify_email("Tester@gmail.com"));
    }

    @Test
    public void verify_id() {
        assertTrue(User.verify_id("1234567890"));
        assertFalse(User.verify_id("012345"));
    }
}