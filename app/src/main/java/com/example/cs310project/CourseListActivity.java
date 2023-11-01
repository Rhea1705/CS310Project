package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private List<Course> courseList;
    private CourseAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Get the selected department name from the previous activity
        String selectedDepartment = getIntent().getStringExtra("selectedDepartment");
        Log.d("courses", "dept: " + selectedDepartment);

        courseList = new ArrayList<>();

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference departmentRef = database.getReference("departments").child(selectedDepartment);
        departmentRef.child("courses");
        // Reference to the LinearLayout container
        LinearLayout courseListLayout = findViewById(R.id.courseListLayout);


        departmentRef.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseListLayout.removeAllViews();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    String courseName = courseSnapshot.child("name").getValue(String.class);
                    String courseDescription = courseSnapshot.child("description").getValue(String.class);
                    Integer num_enrolled = courseSnapshot.child("num_enrolled").getValue(Integer.class);
                    Log.d("course list", "course name: " + courseName);
                    if (course != null) {
                        createCourseItem(courseListLayout,courseName, courseDescription, num_enrolled);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
    }
    private void createCourseItem(LinearLayout parentLayout, String courseName, String description, Integer num) {
        LayoutInflater inflater = getLayoutInflater();
        View departmentItemView = inflater.inflate(R.layout.course_item, parentLayout, false);

        TextView courseNameTextView = departmentItemView.findViewById(R.id.courseNameTextView);
        courseNameTextView.setText(courseName);

        TextView toggleButton = departmentItemView.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView courseDescription = departmentItemView.findViewById(R.id.courseDescriptionTextView);
                courseDescription.setText(description);
                courseDescription.setVisibility(View.VISIBLE);
                TextView num_enroll = departmentItemView.findViewById(R.id.courseEnrollmentTextView);
                num_enroll.setVisibility(View.VISIBLE);
                num_enroll.setText(num);
                Button enrollBtn = departmentItemView.findViewById(R.id.enrollBtn);
                enrollBtn.setVisibility(View.VISIBLE);

            }
        });

        parentLayout.addView(departmentItemView);
    }
}

//    private void toggleDescriptionVisibility() {
//        if (isDescriptionVisible) {
//            courseDescriptionTextView.setVisibility(View.GONE);
//            toggleButton.setText("Show Description");
//        } else {
//            courseDescriptionTextView.setVisibility(View.VISIBLE);
//            toggleButton.setText("Hide Description");
//        }
//        isDescriptionVisible = !isDescriptionVisible;
//    }