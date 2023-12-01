package com.example.cs310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private EditText workloadEditText, scoreEditText, commentsEditText;
    private RatingBar courseEditText, profRating;
    private RadioGroup attendanceSpinner;
    private Button submitButton;
    private DatabaseReference databaseReference;
    private DatabaseReference reviewref;
    private String attendance, late, workload;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_review);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.classes);
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
        courseEditText = findViewById(R.id.edit_text_course);
        profRating = findViewById(R.id.ratingProf);
        commentsEditText = findViewById(R.id.edit_text_comments);
        submitButton = findViewById(R.id.submit_review_button);
        attendanceSpinner = findViewById(R.id.attendance_spinner);
        String selectedCourse= getIntent().getStringExtra("selectedCourse");
        String department = getIntent().getStringExtra("department");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reviewref = database.getReference("departments").child(department).child("courses").child(selectedCourse).child("reviews");
        mAuth = FirebaseAuth.getInstance();

        RadioGroup radioGroupLateHomework = findViewById(R.id.radioGroupLateHomework);
        radioGroupLateHomework.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    late = selectedRadioButton.getText().toString();
                }
            }
        });
        RadioGroup workLoadOptions = findViewById(R.id.workloadOptions);
        workLoadOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    workload = selectedRadioButton.getText().toString();
                }
            }
        });
        attendanceSpinner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    attendance = selectedRadioButton.getText().toString();
                }
            }
        });

        submitButton.setOnClickListener(view -> submitReview());
    }

    private void submitReview() {
        Integer course = (int) courseEditText.getRating();
        Integer prof = (int) profRating.getRating();
        String comments = commentsEditText.getText().toString().trim();
        int up_count = 0;
        int down_count = 0;
        Review review = new Review(attendance,comments,workload,course,prof,late,up_count,down_count);

        if (review.getWorkload() != null && review.getAttendance() != null
        && review.checkCommentSize(review.comments) && review.getLate() != null) {

            // For demonstration purposes, let's consider a fixed userID

            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String uuid = firebaseUser.getUid();
            //
            //String key = databaseReference.push().getKey();



            if (uuid != null) {
                reviewref.child(uuid).setValue(review);
            }

            Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
        Intent intent  = new Intent(ReviewActivity.this, DepartmentsActivity.class);
        startActivity(intent);
    }
}
