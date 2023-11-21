package com.example.cs310project;

import static org.junit.Assert.*;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class CourseTest {
    @Test
    public void testSetName() {
        // Given
        Course course = new Course();

        // When
        course.setName("Newtestcourse");

        // Then
        assertEquals("Newtestcourse", course.getName());
    }

    @Test
    public void testSetNumEnrolled() {
        // Given
        Course course = new Course();

        // When
        course.setNumEnrolled(15);

        // Then
        assertEquals(Integer.valueOf(15), course.getNumEnrolled());
    }

    @Test
    public void testSetRoster() {
        // Given
        Course course = new Course();
        List<User> roster = new ArrayList<>();
        User user = new User("TestUser", "tester1", "password223", "1234567880", "tester@usc.edu", "2393835793", "Staff", "test_uid");
        roster.add(user);

        // When
        course.setRoster(roster);

        // Then
        assertEquals(roster, course.getRoster());
    }

    @Test
    public void testSetDepartment() {
        // Given
        Course course = new Course();

        // When
        course.setDepartment("Data Science");

        // Then
        assertEquals("Data Science", course.getDepartment());
    }
}