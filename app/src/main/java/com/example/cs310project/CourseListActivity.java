package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseListActivity extends AppCompatActivity {

    private List<Course> courseList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);

        // Get the selected department name from the previous activity
        String selectedDepartment = getIntent().getStringExtra("selectedDepartment");
        Log.d("courses", "dept: " + selectedDepartment);

        courseList = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference departmentRef = database.getReference("departments").child(selectedDepartment);
        departmentRef.child("courses");
        LinearLayout courseListLayout = findViewById(R.id.courseListLayout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.departmentListLayout);
        Intent chatIntent = new Intent(this, ChatActivity.class);
        Intent classesIntent = new Intent(this, DepartmentsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.chat) {
                    startActivity(chatIntent);
                }
                else if (itemId == R.id.classes) {
                    startActivity(classesIntent);
                }
                else if (itemId == R.id.profile) {
                    startActivity(profileIntent);
                }
                return true;
            }
        });
        departmentRef.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseListLayout.removeAllViews();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String courseName = courseSnapshot.child("name").getValue(String.class);
                    Integer num_enrolled = courseSnapshot.child("num_enrolled").getValue(Integer.class);
                    Log.d("course list", "course name: " + courseName);

                        createCourseItem(courseListLayout,courseName, num_enrolled);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
    }
    private void createCourseItem(LinearLayout parentLayout, String courseName, Integer num) {
        LayoutInflater inflater = getLayoutInflater();
        View departmentItemView = inflater.inflate(R.layout.course_item, parentLayout, false);

        TextView courseNameTextView = departmentItemView.findViewById(R.id.courseNameTextView);
        courseNameTextView.setText(courseName);

        TextView toggleButton = departmentItemView.findViewById(R.id.toggleButton);
        TextView reviews = departmentItemView.findViewById(R.id.reviews);
        TextView roster = departmentItemView.findViewById(R.id.roster);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roster.setVisibility(View.VISIBLE);
                reviews.setVisibility(View.VISIBLE);

                //navigate to separate roster page
                roster.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(CourseListActivity.this, Roster.class);
                        startActivity(intent);

                    }
                });
                TextView num_enroll = departmentItemView.findViewById(R.id.courseEnrollmentTextView);
                num_enroll.setVisibility(View.VISIBLE);
                String number = String.valueOf(num);
                num_enroll.setText("enrolled: " +number);
                Button enrollBtn = departmentItemView.findViewById(R.id.enrollBtn);
                enrollBtn.setVisibility(View.VISIBLE);
                enrollBtn.setOnClickListener(new View.OnClickListener() {;
                    public void onClick(View view) {
                        if(enrollBtn.getText()=="Enroll") {
                            Integer new_num = num+1;
                            num_enroll.setText("enrolled: " + new_num);
                            enrollBtn.setText("Unenroll");
                        }
                        else {
                            Integer new_num = num-1;
                            num_enroll.setText("enrolled: " + new_num);
                        }

                    }
                });

            }
        });

        roster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseListActivity.this, Roster.class);
                intent.putExtra("selectedCourse", courseName);
                startActivity(intent);
            }
        });

        parentLayout.addView(departmentItemView);
    }
}
