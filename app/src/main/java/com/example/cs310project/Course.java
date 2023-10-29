package com.example.cs310project;

import java.util.List;

public class Course {
    String name;
    Integer num_enrolled;
    List<String> roster;
    String department;
    public Course() {

    }
    public String getName() {
        return name;
    }

    // Setter for 'name'
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

    // Setter for 'department'
    public void setDepartment(String department) {
        this.department = department;
    }
}



}