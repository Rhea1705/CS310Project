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
        course.setNumEnrolled(25);

        // Then
        assertEquals(Integer.valueOf(25), course.getNumEnrolled());
    }

    @Test
    public void testSetRoster() {
        // Given
        Course course = new Course();
        List<User> roster = new ArrayList<>();
        User user = new User("TestUser", "test@example.com", "password", "1234567890", "123456789", "student", "username", "test_uid");
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
        course.setDepartment("Computer Science");

        // Then
        assertEquals("Computer Science", course.getDepartment());
    }
}