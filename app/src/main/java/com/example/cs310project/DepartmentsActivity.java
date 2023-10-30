package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

        // Create a list of department names (strings)
        List<String> departmentNames = new ArrayList<>();

        DepartmentAdapter adapter = new DepartmentAdapter(departmentNames);
        recyclerView.setAdapter(adapter);

        // Add a ValueEventListener to fetch department names from Firebase
        departmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                departmentNames.clear();
                for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                    String departmentName = departmentSnapshot.child("name").getValue(String.class);
                    departmentNames.add(departmentName);
                    Log.d("myapp", "print " + departmentName);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
            }
        });

        adapter.setOnItemClickListener(new DepartmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String selectedDepartment = departmentNames.get(position);
                Intent intent = new Intent(DepartmentsActivity.this, CourseListActivity.class);
                intent.putExtra("selectedDepartment", selectedDepartment);
                startActivity(intent);
            }
        });
    }
}

