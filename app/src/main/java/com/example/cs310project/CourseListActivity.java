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

        // Initialize Firebase
        FirebaseDatabase database =FirebaseDatabase.getInstance();
        DatabaseReference departmentRef = database.getReference("departments").child(selectedDepartment);
        DatabaseReference courseref = departmentRef.child("courses");
        // Reference to the LinearLayout container
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


        courseref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseListLayout.removeAllViews();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String courseName = courseSnapshot.child("name").getValue(String.class);
                    Integer num = courseSnapshot.child("num_enrolled").getValue(Integer.class);
                    Course currCourse = new Course();
                    currCourse.setName(courseName);
                    currCourse.setNumEnrolled(num);
                    Log.d("course list", "course name: " + courseName);

                        createCourseItem(courseListLayout,courseName,courseref, currCourse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
    }
    private void createCourseItem(LinearLayout parentLayout, String courseName, DatabaseReference courseref, Course currCourse) {
        LayoutInflater inflater = getLayoutInflater();
        View departmentItemView = inflater.inflate(R.layout.course_item, parentLayout, false);

        TextView courseNameTextView = departmentItemView.findViewById(R.id.courseNameTextView);
        courseNameTextView.setText(courseName);
        Integer num = currCourse.getNumEnrolled();

        TextView toggleButton = departmentItemView.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView reviews = departmentItemView.findViewById(R.id.reviews);
                TextView roster = departmentItemView.findViewById(R.id.roster);
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
                String number = String.valueOf(currCourse.getNumEnrolled());
                num_enroll.setText("enrolled: " +number);
                Button enrollBtn = departmentItemView.findViewById(R.id.enrollBtn);
                enrollBtn.setText("Enroll");
                enrollBtn.setVisibility(View.VISIBLE);
                enrollBtn.setOnClickListener(new View.OnClickListener() {;
                    public void onClick(View view) {

                        if(enrollBtn.getText().equals("Enroll")) {
                            int new_num = currCourse.getNumEnrolled() + 1;
                            currCourse.setNumEnrolled(new_num);
                            List<User> ros = currCourse.getRoster();
                            addStudentToRoster(ros,currCourse,"add",courseref);
                            num_enroll.setText("enrolled: " + new_num );
                            courseref.child(courseName).child("num_enrolled").setValue(new_num);
                            enrollBtn.setText("Unenroll");
                        }
                        else {
                            int new_num = currCourse.getNumEnrolled() - 1;
                            currCourse.setNumEnrolled(new_num);
                            List<User> ros = currCourse.getRoster();
                            Log.d("Courseapp",ros.get(0).getName());
                            addStudentToRoster(ros,currCourse,"remove",courseref);
                            courseref.child(courseName).child("num_enrolled").setValue(new_num);
                            num_enroll.setText("enrolled: " + new_num);
                            enrollBtn.setText("Enroll");

                        }

                        //vidit add code here for current user
                    }
                });

            }
        });

        parentLayout.addView(departmentItemView);
    }



    private void addStudentToRoster(List<User> ros,Course currCourse,String whattodo,DatabaseReference courseref){
        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference ref = base.getReference();
        DatabaseReference childref = ref.child("UserList");
        DatabaseReference usename = childref.child(Access.username);

        usename.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    return;
                }
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String phone_number = snapshot.child("phone_number").getValue(String.class);
                String usc_id = snapshot.child("usc_id").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
                Log.d("My app",name + email);
                User user = new User(name,email,password,phone_number,usc_id,role,Access.username);
                if(whattodo.equals("add")){
                        ros.add(user);
                }
                else if(whattodo.equals("remove")){
                    Log.d("Courseapp","enters remove");
                    Log.d("Courseapp",ros.get(0).getEmail());
                    for(User use: ros) {
                        if (use.getUsername().equals(Access.username)) {
                            ros.remove(use);
                        }
                    }
                }
                currCourse.setRoster(ros);
//                courseref.child(currCourse.name).child("roster").setValue(Access.username);
                DatabaseReference usename = courseref.child(currCourse.name).child("roster").child(Access.username);
                usename.setValue(ros);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
