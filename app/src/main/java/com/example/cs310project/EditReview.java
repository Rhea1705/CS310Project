package com.example.cs310project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EditReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        String attendance = getIntent().getStringExtra("attendance");
        String comments = getIntent().getStringExtra("comments");
        String workload = getIntent().getStringExtra("workload");
        Integer score = getIntent().getIntExtra("rating",0);
        Integer profRating = getIntent().getIntExtra("prof",0);
        String late = getIntent().getStringExtra("late");
        Integer up_count = 0;
        Integer down_count = 0;
        Review review = new Review(attendance,comments,workload,score,profRating,late,up_count,down_count);
    }
}