package com.example.cs310project;

import java.util.List;

public class Department {
    String name;
    List<Course> courses;
    public String getName() {
        return name;
    }

    // Setter for 'name's
    public void setName(String name) {
        this.name = name;
    }
    public List<Course> getCourses() {
        return courses;
    }
}
