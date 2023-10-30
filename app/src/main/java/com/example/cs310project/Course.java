package com.example.cs310project;

import java.util.List;

public class Course {
    String name;
    String description;
    Integer num_enrolled;
    List<String> roster;
    String department;
    boolean expanded;
    public Course(String name, String description, int num_enrolled) {
        this.name = name;
        this.description = description;
        this.num_enrolled = num_enrolled;
        this.expanded = false;
    }
    public String getName() {
        return name;
    }

    // Setter for 'name's
    public void setName(String name) {
        this.name = name;
    }

    // Getter for 'num_enrolled'
    public Integer getNumEnrolled() {
        return num_enrolled;
    }

    // Setter for 'num_enrolled'
    public void setNumEnrolled(Integer num_enrolled) {
        this.num_enrolled = num_enrolled;
    }

    // Getter for 'roster'
    public List<String> getRoster() {
        return roster;
    }

    // Setter for 'roster'
    public void setRoster(List<String> roster) {
        this.roster = roster;
    }

    // Getter for 'department'
    public String getDepartment() {
        return department;
    }
    public String getDescription() {
        return description;
    }

    // Setter for 'department'
    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}

