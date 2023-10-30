package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.csci310finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DepartmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        RecyclerView recyclerView = findViewById(R.id.departmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference departmentsRef = database.getReference("departments");

        // Create a list of departments (you can populate this from Firebase)
        List<Department> departmentList = new ArrayList<>();

        CourseAdapter adapter = new CourseAdapter(Collections.singletonList(departmentList));
        recyclerView.setAdapter(adapter);
        // Add a ValueEventListener to fetch departments from Firebase
        departmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentList.clear();
                for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                    // String departmentName = departmentSnapshot.child("name").getValue(String.class);
                    Department department = departmentSnapshot.getValue(Department.class);
                    departmentList.add(department);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedDepartment = departmentList.get(position).getName();
                Intent intent = new Intent(DepartmentsActivity.this, CourseListActivity.class);
                intent.putExtra("selectedDepartment", selectedDepartment);
                startActivity(intent);
            }
        });
    }
}
