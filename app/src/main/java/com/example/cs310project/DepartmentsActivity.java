package com.example.cs310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DepartmentsActivity extends AppCompatActivity {

    DatabaseReference departmentsRef;
    FirebaseAuth mAuth;
    private ListView courseListView;
    private ArrayAdapter<String> adapter;
    private List<String> courseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.classes);
        Intent chatIntent = new Intent(this, FriendActivity.class);
        Intent classesIntent = new Intent(this, DepartmentsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        mAuth = FirebaseAuth.getInstance();
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

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        departmentsRef = database.getReference("departments");

        // Reference to the LinearLayout container
        LinearLayout departmentListLayout = findViewById(R.id.departmentListLayout);

        Button seeEnrolledCoursesButton = findViewById(R.id.seeEnrolledCoursesButton);

        // Set OnClickListener for the button
        seeEnrolledCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_my_course);

                // Initialize Firebase
                departmentsRef = FirebaseDatabase.getInstance().getReference().child("departments");

                // Initialize views
                TextView titleTextView = findViewById(R.id.titleTextView);
                courseListView = findViewById(R.id.courseListView);

                // Initialize list to hold courses
                courseList = new ArrayList<>();
                adapter = new ArrayAdapter<>(DepartmentsActivity.this, android.R.layout.simple_list_item_1, courseList);
                courseListView.setAdapter(adapter);
                get_course();
            }
        });
        // Add a ValueEventListener to fetch department names from Firebase
        departmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentListLayout.removeAllViews(); // Clear any previous views

                for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                    String departmentName = departmentSnapshot.child("name").getValue(String.class);
                    Log.d("myapp", "name: " + departmentName);
                    if (departmentName != null) {
                        // Create a TextView for the department name
                        createDepartmentItem(departmentListLayout, departmentName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }

        });
    }
    private void get_course() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String key = firebaseUser.getUid();
        departmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                    Log.d("department loop", "dep looping");
                    for (DataSnapshot classSnapshot : departmentSnapshot.child("courses").getChildren()) {
                        Log.d("class loop", "class looping");
                        if (classSnapshot.child("roster").hasChild(key)) {
                            String className = classSnapshot.child("name").getValue(String.class);
                            Log.d("Course", className);
                            if (className != null) {
                                courseList.add(className);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    private void createDepartmentItem(LinearLayout parentLayout, String departmentName) {
        LayoutInflater inflater = getLayoutInflater();
        View departmentItemView = inflater.inflate(R.layout.department_item, parentLayout, false);

        TextView departmentNameTextView = departmentItemView.findViewById(R.id.departmentNameTextView);
        departmentNameTextView.setText(departmentName);

        TextView toggleButton = departmentItemView.findViewById(R.id.toggle);
        departmentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
                Log.d("my app","on click: " + departmentName);
                Intent intent = new Intent(DepartmentsActivity.this, CourseListActivity.class);
                intent.putExtra("selectedDepartment", departmentName);
                startActivity(intent);
            }
        });

        parentLayout.addView(departmentItemView);
    }
}

