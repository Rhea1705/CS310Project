package com.example.cs310project;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
//        String[] attendanceArray = {"Yes", "No"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, attendanceArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        attendanceSpinner.setAdapter(adapter);
        // Initialize Firebase
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        databaseReference = firebaseDatabase.getReference().child("course_reviews");
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
        Integer course = courseEditText.getNumStars();
        Integer prof = profRating.getNumStars();
        String comments = commentsEditText.getText().toString().trim();

        if (!workload.isEmpty()
                && !comments.isEmpty()) {

            // For demonstration purposes, let's consider a fixed userID

            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String uuid = firebaseUser.getUid();
            //
            //String key = databaseReference.push().getKey();
            int up_count = 0;
            int down_count = 0;


            if (uuid != null) {
                Review review = new Review(attendance,comments,workload,course,prof,late,up_count,down_count);
//                reviewref.child(uuid).child("score").setValue(course);
//                reviewref.child(uuid).child("workload").setValue(workload);
//                reviewref.child(uuid).child("attendance").setValue(attendance);
//                reviewref.child(uuid).child("comments").setValue(comments);
//                reviewref.child(uuid).child("prof").setValue(prof);
//                reviewref.child(uuid).child("late").setValue(late);
//                reviewref.child(uuid).child("up_count").setValue(up_count);
//                reviewref.child(uuid).child("down_count").setValue(down_count);
//                reviewref.child(uuid).child("users_who_liked").setValue(null);
//                reviewref.child(uuid).child("users_who_disliked").setValue(null);
                reviewref.child(uuid).setValue(review);
            }

            Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
//            courseEditText.setNumStars(0);
//            workloadEditText.setText("");
//            scoreEditText.setText("");
//            commentsEditText.setText("");
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
