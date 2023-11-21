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
    public void checkCommentSize() {
        Review testReview = new Review("Yes", "it was amazing", "light", 4,3,"Yes",0,0);
        assertTrue(testReview.checkCommentSize(testReview.comments));
        Review testReview2 = new Review("Yes", "it was an amazing experience studying in this class.", "light", 4,3,"Yes",0,0);
        assertFalse(testReview2.checkCommentSize(testReview2.comments));

    }
    @Test
    public void isValidTest() {
        Review testReview = new Review("Yes", "it was amazing", "light", 4,3,"Yes",0,0);
        assertTrue(testReview.isValid());
        Review testReview2 = new Review("", "it was amazing", "light", 4,3,"Yes",0,0);
        assertFalse(testReview2.isValid());
    }
    @Test
    void scoreNotNull_valid_invalid() {
        //valid
        Review review = new Review("", "", "", 1, 1, "", 0, 0);
        assertTrue(review.scoreNotNull());
        //invalid
        Review review2 = new Review("", "", "", null, 1, "", 0, 0);
        assertFalse(review2.scoreNotNull());
    }



    @Test
    void profNotNull_valid_invalid() {
        Review review = new Review("", "", "", 1, 1, "", 0, 0);
        assertTrue(review.profNotNull());
        Review review2 = new Review("", "", "", 1, null, "", 0, 0);
        assertFalse(review2.profNotNull());
    }
}