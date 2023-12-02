package com.example.cs310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.util.List;

public class EnrolledCourses extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference departmentsRef;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        LinearLayout courseListLayout = findViewById(R.id.courseListLayout);
        courseListLayout.removeAllViews();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.enrolled);
        Intent chatIntent = new Intent(this, FriendActivity.class);
        Intent classesIntent = new Intent(this, DepartmentsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        Intent enrolledIntent = new Intent(this, EnrolledCourses.class);
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
                else if (itemId == R.id.enrolled) {
                    startActivity(enrolledIntent);
                }
                return true;
            }
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String key = firebaseUser.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        departmentsRef = database.getReference("departments");
        //DatabaseReference courseref = departmentsRef.child("courses");
        departmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot departmentSnapshot : dataSnapshot.getChildren()) {
                    Log.d("department loop", "dep looping");
                    String deptName = departmentSnapshot.child("name").getValue(String.class);
                    for (DataSnapshot classSnapshot : departmentSnapshot.child("courses").getChildren()) {
                        Log.d("class loop", "class looping");
                        if (classSnapshot.child("roster").hasChild(key)) {
                            String courseName = classSnapshot.child("name").getValue(String.class);
                            Log.d("Course", courseName);
                            if (courseName != null) {
//                                courseList.add(className);
                                LayoutInflater inflater = getLayoutInflater();
                                View courseItemView = inflater.inflate(R.layout.course_item, courseListLayout, false);

                                TextView courseNameTextView = courseItemView.findViewById(R.id.courseNameTextView);
                                TextView courseEnrollTextView = courseItemView.findViewById(R.id.courseEnrollmentTextView);
                                courseNameTextView.setText(courseName);
                                Integer num_enrolled = classSnapshot.child("num_enrolled").getValue(Integer.class);
                                courseEnrollTextView.setText("Enrolled: "+ num_enrolled);
                                courseListLayout.addView(courseItemView);
                                //all items visible
                                TextView reviews = courseItemView.findViewById(R.id.reviews);
                                TextView roster = courseItemView.findViewById(R.id.roster);
                                Button enrollBtn = courseItemView.findViewById(R.id.enrollBtn);
                                enrollBtn.setText("Unenroll");
                                reviews.setVisibility(View.VISIBLE);
                                roster.setVisibility(View.VISIBLE);
                                enrollBtn.setVisibility(View.VISIBLE);
                                String course_Name = classSnapshot.child("name").getValue(String.class);
                                Integer num = classSnapshot.child("num_enrolled").getValue(Integer.class);
                                Course currCourse = new Course();
                                currCourse.setName(course_Name);
                                currCourse.setNumEnrolled(num);
                                currCourse.setDepartment(deptName);
                                DatabaseReference courseref = departmentsRef.child(deptName).child("courses");
                                List<User> ros = currCourse.getRoster();
                                DataSnapshot rosterSnapshot = classSnapshot.child("roster");
                                for (DataSnapshot studentSnapshot : rosterSnapshot.getChildren()) {
                                    String uid = studentSnapshot.getKey();
                                    String name_topopulate = studentSnapshot.child("name").getValue(String.class);
                                    String email_topopulate = studentSnapshot.child("email").getValue(String.class);
                                    String password_topopulate = studentSnapshot.child("password").getValue(String.class);
                                    String phone_number_topopulate = studentSnapshot.child("phone_number").getValue(String.class);
                                    String usc_id_topopulate = studentSnapshot.child("usc_id").getValue(String.class);
                                    String role_topopulate = studentSnapshot.child("role").getValue(String.class);
                                    String username_topopulate = studentSnapshot.child("username").getValue(String.class);
                                    User adduser = new User(name_topopulate, email_topopulate, password_topopulate, phone_number_topopulate, usc_id_topopulate, role_topopulate, username_topopulate, uid);
                                    ros.add(adduser);

                                }
                                currCourse.setRoster(ros);



                                reviews.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(EnrolledCourses.this, AllReviewsActivity.class);
                                        intent.putExtra("selectedCourse", courseName);
                                        intent.putExtra("department", deptName);
                                        startActivity(intent);
                                    }
                                });

                                roster.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View view) {
                                        Intent intent = new Intent(EnrolledCourses.this, Roster.class);
                                        intent.putExtra("selectedCourse", courseName);
                                        intent.putExtra("department", deptName);
                                        startActivity(intent);

                                    }
                                });
                                TextView num_enroll = courseItemView.findViewById(R.id.courseEnrollmentTextView);
                                num_enroll.setVisibility(View.VISIBLE);
                                String number = String.valueOf(num_enrolled);
                                num_enroll.setText("enrolled: " +number);
                                enrollBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        int new_num = Coursemanager.decrease_rostersize(num_enrolled);
                                        currCourse.setNumEnrolled(new_num);

                                        //addStudentToRoster(currCourse.getRoster(),currCourse,"remove",courseref);
                                        Coursemanager.addStudentToRoster(currCourse.getRoster(),currCourse,"remove",courseref);
                                        courseref.child(courseName).child("num_enrolled").setValue(new_num);
                                        finish();
                                        startActivity(getIntent());
                                    }
                                });

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
