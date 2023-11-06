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

public class ReviewActivity extends AppCompatActivity {

    private EditText workloadEditText, scoreEditText, commentsEditText;
    private RatingBar courseEditText;
    private RadioGroup attendanceSpinner;
    private Button submitButton;
    private DatabaseReference databaseReference;
    private DatabaseReference reviewref;
    private String attendance, late;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_review);

        courseEditText = findViewById(R.id.edit_text_course);
        workloadEditText = findViewById(R.id.edit_text_workload);
//        scoreEditText = findViewById(R.id.edit_text_score);
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
        attendanceSpinner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    attendance = selectedRadioButton.getText().toString();
                }
            }
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        submitButton.setOnClickListener(view -> submitReview());
    }

    private void submitReview() {
        Integer course = courseEditText.getNumStars();
        String workload = workloadEditText.getText().toString().trim();
        int score = Integer.parseInt(scoreEditText.getText().toString());
        String comments = commentsEditText.getText().toString().trim();

        if (!workload.isEmpty()
                && !comments.isEmpty()) {

            // For demonstration purposes, let's consider a fixed userID
            String userID = "user123";
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String uuid = firebaseUser.getUid();
            //
            //String key = databaseReference.push().getKey();

            if (uuid != null) {
                reviewref.child(uuid).child("userID").setValue(userID);
                reviewref.child(uuid).child("course").setValue(course);
                reviewref.child(uuid).child("workload").setValue(workload);
                reviewref.child(uuid).child("score").setValue(score);
                reviewref.child(uuid).child("attendance").setValue(attendance);
                reviewref.child(uuid).child("comments").setValue(comments);
            }

            Toast.makeText(this, "Review submitted!", Toast.LENGTH_SHORT).show();
            courseEditText.setNumStars(0);
            workloadEditText.setText("");
            scoreEditText.setText("");
            commentsEditText.setText("");
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }
}
