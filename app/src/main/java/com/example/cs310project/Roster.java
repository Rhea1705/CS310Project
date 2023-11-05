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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Roster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);

        String selectedCourse= getIntent().getStringExtra("selectedCourse");
        String department = getIntent().getStringExtra("department");
        Log.d("roster", "course: " + selectedCourse);
        Log.d("roster", "dept: " + department);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rosterRef = database.getReference("departments").child(department).child("courses").child(selectedCourse).child("roster");
        LinearLayout rosterList = findViewById(R.id.rosterList);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.rosterList);
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
        rosterRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rosterList.removeAllViews();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String name = courseSnapshot.child("name").getValue(String.class);
                    Log.d("roster", "student Name: " + name);
                    if (name != null) {
                        createRosterItem(rosterList, name);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });

    }
    private void createRosterItem(LinearLayout parentLayout, String studentName) {
        LayoutInflater inflater = getLayoutInflater();
        View rosterView = inflater.inflate(R.layout.roster_item, parentLayout, false);
        TextView studentNameView = rosterView.findViewById(R.id.name);
        studentNameView.setText(studentName);
        Button chatBtn = rosterView.findViewById(R.id.chat);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
                Intent intent = new Intent(Roster.this, ChatActivity.class);
                intent.putExtra("selectedStudent", studentName);
                startActivity(intent);
            }
        });
        parentLayout.addView(rosterView);

    }
}