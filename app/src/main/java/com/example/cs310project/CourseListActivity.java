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
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

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

public class CourseListActivity extends AppCompatActivity {

    private List<Course> courseList;
    private RecyclerView recyclerView;
    FirebaseAuth mAuth;


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
        mAuth = FirebaseAuth.getInstance();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.chat) {
                    item.setIcon(R.drawable.chat_selected);
                    startActivity(chatIntent);
                }
                else if (itemId == R.id.classes) {
                    startActivity(classesIntent);
                    item.setIcon(R.drawable.classes_selcted);
                }
                else if (itemId == R.id.profile) {
                    startActivity(profileIntent);
                    item.setIcon(R.drawable.user_selected);
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
                    currCourse.setDepartment(selectedDepartment);
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

        Button toggleButton = departmentItemView.findViewById(R.id.toggleButton);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView reviews = departmentItemView.findViewById(R.id.reviews);
                TextView roster = departmentItemView.findViewById(R.id.roster);
                Button enrollBtn = departmentItemView.findViewById(R.id.enrollBtn);

                if(enrollBtn.getText()=="Enroll") {
                    roster.setVisibility(View.VISIBLE);
                }
                reviews.setVisibility(View.VISIBLE);

                //navigate to separate roster page
                reviews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CourseListActivity.this, AllReviewsActivity.class);
                        intent.putExtra("selectedCourse", currCourse.getName());
                        intent.putExtra("department", currCourse.getDepartment());
                        startActivity(intent);
                    }
                });
                roster.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(CourseListActivity.this, Roster.class);
                        intent.putExtra("selectedCourse", currCourse.getName());
                        intent.putExtra("department", currCourse.getDepartment());
                        startActivity(intent);

                    }
                });
                TextView num_enroll = departmentItemView.findViewById(R.id.courseEnrollmentTextView);
                num_enroll.setVisibility(View.VISIBLE);
                String number = String.valueOf(currCourse.getNumEnrolled());
                num_enroll.setText("enrolled: " +number);
                courseref.child(currCourse.getName()).child("roster").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean there= false;
                        List<User> ros = currCourse.getRoster();
                        for(DataSnapshot courseSnapshot : snapshot.getChildren()){

                            String uid = courseSnapshot.getKey();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String uuid = firebaseUser.getUid();
                            Log.d("rosterapp",uid);
                            if(uid.equals(uuid)){
                                there = true;
                            }
                            String name_topopulate = snapshot.child("name").getValue(String.class);
                            String email_topopulate = snapshot.child("email").getValue(String.class);
                            String password_topopulate = snapshot.child("password").getValue(String.class);
                            String phone_number_topopulate = snapshot.child("phone_number").getValue(String.class);
                            String usc_id_topopulate = snapshot.child("usc_id").getValue(String.class);
                            String role_topopulate = snapshot.child("role").getValue(String.class);
                            String username_topopulate = snapshot.child("username").getValue(String.class);
                            User adduser = new User(name_topopulate,email_topopulate,password_topopulate,phone_number_topopulate,usc_id_topopulate,role_topopulate,username_topopulate,uid);
                            ros.add(adduser);
                        }
                        if(there){
                            roster.setVisibility(View.VISIBLE);
                            enrollBtn.setText("Unenroll");
                        }
                        else{
                            enrollBtn.setText("Enroll");
                        }
                        currCourse.setRoster(ros);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                enrollBtn.setVisibility(View.VISIBLE);
                enrollBtn.setOnClickListener(new View.OnClickListener() {;
                    public void onClick(View view) {

                        if(enrollBtn.getText().equals("Enroll")) {
                            int new_num = currCourse.getNumEnrolled() + 1;
                            currCourse.setNumEnrolled(new_num);

                            addStudentToRoster(currCourse.getRoster(),currCourse,"add",courseref);
                            num_enroll.setText("Enrolled: " + new_num );
                            courseref.child(courseName).child("num_enrolled").setValue(new_num);
                            enrollBtn.setText("Unenroll");
                            roster.setVisibility(View.VISIBLE);
                        }
                        else {
                            int new_num = currCourse.getNumEnrolled() - 1;
                            currCourse.setNumEnrolled(new_num);

                            addStudentToRoster(currCourse.getRoster(),currCourse,"remove",courseref);
                            courseref.child(courseName).child("num_enrolled").setValue(new_num);
                            num_enroll.setText("Enrolled: " + new_num);
                            enrollBtn.setText("Enroll");
                            roster.setVisibility(View.INVISIBLE);

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
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uuid = firebaseUser.getUid();
        DatabaseReference useref = childref.child(uuid);

        useref.addListenerForSingleValueEvent(new ValueEventListener() {
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
                String username = snapshot.child("username").getValue(String.class);
                Log.d("My app",name + email);

                if(whattodo.equals("add")){
                    User user = new User(name,email,password,phone_number,usc_id,role,username,uuid);
                    ros.add(user);
                    courseref.child(currCourse.name).child("roster").child(uuid).setValue(user);
                }
                else if(whattodo.equals("remove")){
                    User remove = null;
                    Log.d("Courseapp","enters remove");
                    for(User use: ros) {

                        if (use.getUid().equals(uuid)) {
                            remove = use;

                        }
                    }
                    if(remove != null){
                        ros.remove(remove);
                        courseref.child(currCourse.name).child("roster").child(uuid).removeValue();
                    }
                }
                currCourse.setRoster(ros);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
