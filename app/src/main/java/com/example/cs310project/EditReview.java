package com.example.cs310project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class EditReview extends AppCompatActivity {
    private RatingBar courseEditText, profRating;
    String newLate, newWorkload, newAttendance;
    Integer newRating, newProf;
    private RadioGroup attendanceSpinner;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        String attendance = getIntent().getStringExtra("attendance");
        String comments = getIntent().getStringExtra("comments");
        String workload = getIntent().getStringExtra("workload");
        Integer score = getIntent().getIntExtra("rating", 0);
        Integer profRat = getIntent().getIntExtra("prof", 0);
        String late = getIntent().getStringExtra("late");
        Integer up_count = 0;
        Integer down_count = 0;
        Review review = new Review(attendance, comments, workload, score, profRat, late, up_count, down_count);
        // populate form
        courseEditText = findViewById(R.id.edit_text_course);
        courseEditText.setNumStars(score);
        profRating = findViewById(R.id.ratingProf);
        profRating.setNumStars(profRat);
        EditText commentsEditText = findViewById(R.id.edit_text_comments);
        commentsEditText.setText(comments);
        attendanceSpinner = findViewById(R.id.attendance_spinner);
        RadioButton att;
        if (attendance == "Yes") {
            att = findViewById(R.id.radioButtonYes);
            att.setChecked(true);
        } else {
            att = findViewById(R.id.radioButtonNo);
            att.setChecked(true);
        }
        RadioButton work;
        if (workload == "Light") {
            work = findViewById(R.id.workloadLight);
            work.setChecked(true);
        } else if (workload == "Medium") {
            work = findViewById(R.id.workloadMedium);
            work.setChecked(true);
        } else {
            work = findViewById(R.id.workloadHeavy);
            work.setChecked(true);
        }
        RadioButton latehw;
        if (late == "Yes") {
            latehw = findViewById(R.id.radioButtonLateYes);
            latehw.setChecked(true);
        } else {
            latehw = findViewById(R.id.radioButtonLateNo);
            latehw.setChecked(true);
        }

        //update form
        submitButton = findViewById(R.id.submit_review_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRating = courseEditText.getNumStars();
                newProf = profRating.getNumStars();
                String newComments = String.valueOf(commentsEditText.getText());
                RadioGroup radioGroupLateHomework = findViewById(R.id.radioGroupLateHomework);
                radioGroupLateHomework.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton = findViewById(checkedId);
                        if (selectedRadioButton != null) {
                            newLate = selectedRadioButton.getText().toString();
                        }
                    }
                });
                RadioGroup workLoadOptions = findViewById(R.id.workloadOptions);
                workLoadOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton = findViewById(checkedId);
                        if (selectedRadioButton != null) {
                            newWorkload = selectedRadioButton.getText().toString();
                        }
                    }
                });
                attendanceSpinner.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton selectedRadioButton = findViewById(checkedId);
                        if (selectedRadioButton != null) {
                            newAttendance = selectedRadioButton.getText().toString();
                        }
                    }
                });
                Review review = new Review(newAttendance, newComments, newWorkload, newRating, newProf,newLate,0,0);
            }
        });

    }
}