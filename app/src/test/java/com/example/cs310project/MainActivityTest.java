package com.example.cs310project;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void containsusc_edu() {
        MainActivity mainActivity = mock(MainActivity.class);
        assertTrue(mainActivity.containsusc_edu("tester@usc.edu"));
    }
}
