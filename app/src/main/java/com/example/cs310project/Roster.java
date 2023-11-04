package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

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
        Log.d("roster", "course: " + selectedCourse);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference courseRef = database.getReference("roster").child(selectedCourse);
        LinearLayout rosterList = findViewById(R.id.rosterList);

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
        courseRef.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rosterList.removeAllViews();
//                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
//                    String courseName = courseSnapshot.child("name").getValue(String.class);
//                    Integer num_enrolled = courseSnapshot.child("num_enrolled").getValue(Integer.class);
//                    Log.d("course list", "course name: " + courseName);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
    }
}