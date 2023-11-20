package com.example.cs310project;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReviewTest {

    @Test
    public void attendanceVal() {
        Review testReview = new Review("Yes", "No", "light", 4,3,"Yes",0,0);
        assertTrue(testReview.attendanceNotNull());
        Review testReview2 = new Review("Maybe", "No", "light", 4,3,"Yes",0,0);
        assertFalse(testReview2.attendanceNotNull());
    }

    @Test
    public void getComments() {
    }

    @Test
    public void getWorkload() {
    }

    @Test
    public void getScore() {
    }

    @Test
    public void getProfRating() {
    }

    @Test
    public void getLate() {
    }

    @Test
    public void checkCommentSize() {
        Review testReview = new Review("Yes", "it was amazing", "light", 4,3,"Yes",0,0);
        assertTrue(testReview.checkCommentSize(testReview.comments));
        Review testReview2 = new Review("Yes", "it was an amazing experience studying in this class.", "light", 4,3,"Yes",0,0);
        assertFalse(testReview2.checkCommentSize(testReview2.comments));

    }
}