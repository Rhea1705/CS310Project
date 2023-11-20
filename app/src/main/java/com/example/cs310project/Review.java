package com.example.cs310project;

import java.util.ArrayList;
import java.util.List;

public class Review {
    String attendance;
    String comments;
    String workload;
    Integer score;
    Integer profRating;
    String late;
    Integer up_count;
    Integer down_count;

    List<String> users_who_liked = new ArrayList<>();
    List<String> users_who_disliked = new ArrayList<>();

    public List<String> getUsers_who_liked() {
        return users_who_liked;
    }

    public void setUsers_who_liked(List<String> users_who_liked) {
        this.users_who_liked = users_who_liked;
    }

    public List<String> getUsers_who_disliked() {
        return users_who_disliked;
    }

    public void setUsers_who_disliked(List<String> users_who_disliked) {
        this.users_who_disliked = users_who_disliked;
    }

    public Review(String attendance, String comments, String workload, Integer score, Integer profRating, String late, Integer up_count, Integer down_count) {
        this.attendance = attendance;
        this.comments = comments;
        this.workload = workload;
        this.score = score;
        this.profRating = profRating;
        this.late = late;
        this.up_count = up_count;
        this.down_count = down_count;

    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getWorkload() {
        return workload;
    }

    public void setWorkload(String workload) {
        this.workload = workload;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getProfRating() {
        return profRating;
    }

    public void setProfRating(Integer profRating) {
        this.profRating = profRating;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public Integer getUp_count() {
        return up_count;
    }

    public void setUp_count(Integer up_count) {
        this.up_count = up_count;
    }

    public Integer getDown_count() {
        return down_count;
    }

    public void setDown_count(Integer down_count) {
        this.down_count = down_count;
    }
    public boolean checkCommentSize(String comments) {
        if(comments.length() <= 20 || comments.length() == 0) {
            return true;
        }
        return false;
    }
    public boolean attendanceNotNull() {
        if(attendance != null ) {
            if(attendance == "Yes" || attendance== "No") {
                return true;
            }
        }
        return false;
    }
    public boolean scoreNotNull() {
        if(score == null ) {
            return false;
        }
        return true;
    }
    public boolean profNotNull() {
        if(profRating == null ) {
            return false;
        }
        return true;
    }
    public boolean isValid() {
        if (workload != "" && score != null && attendance != ""
                && this.checkCommentSize(comments) && late != "" && profRating != null) {
            return true;
        }
        return false;
    }
}
