package com.example.cs310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DepartmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference departmentsRef = database.getReference("departments");

        // Reference to the LinearLayout container
        LinearLayout departmentListLayout = findViewById(R.id.departmentListLayout);

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
    private void createDepartmentItem(LinearLayout parentLayout, String departmentName) {
        LayoutInflater inflater = getLayoutInflater();
        View departmentItemView = inflater.inflate(R.layout.department_item, parentLayout, false);

        TextView departmentNameTextView = departmentItemView.findViewById(R.id.departmentNameTextView);
        departmentNameTextView.setText(departmentName);

        TextView toggleButton = departmentItemView.findViewById(R.id.toggle);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
                Intent intent = new Intent(DepartmentsActivity.this, CourseListActivity.class);
                intent.putExtra("selectedDepartment", departmentName);
                startActivity(intent);
            }
        });

        parentLayout.addView(departmentItemView);
    }
}
