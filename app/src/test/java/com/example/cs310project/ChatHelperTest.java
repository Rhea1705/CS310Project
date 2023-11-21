package com.example.cs310project;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ChatHelperTest {

    @Test
    public void getFirst() {
        ChatHelper chatHelper = new ChatHelper();

        assertEquals("A", chatHelper.getFirst("A", "B"));
    }

    @Test
    public void getSecond() {
        ChatHelper chatHelper = new ChatHelper();

        assertEquals("B", chatHelper.getSecond("A", "B"));
    }

    @Test
    public void checkKey(){
        ChatHelper chatHelper = new ChatHelper();

        assertTrue(chatHelper.checkNull("keyTester"));
        assertFalse(chatHelper.checkNull(null));
    }
}