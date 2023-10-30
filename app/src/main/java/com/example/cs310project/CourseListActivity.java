package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
        String selectedDepartment = getIntent().getStringExtra("department");

        courseList = new ArrayList<>();
        adapter = new CourseAdapter(Collections.singletonList(courseList));

        recyclerView = findViewById(R.id.classesRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load classes for the selected department from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("courses");
        databaseReference.orderByChild("department").equalTo(selectedDepartment).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    Course course = courseSnapshot.getValue(Course.class);
                    if (course != null) {
                        courseList.add(course);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
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