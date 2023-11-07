package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditReview extends AppCompatActivity {
    private RatingBar courseEditText, profRating;
    String newLate = "", newWorkload = "", newAttendance = "";
    Integer newRating, newProf;
    private RadioGroup attendanceSpinner;
    private Button submitButton;
    FirebaseDatabase base;
    DatabaseReference ref;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_review);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.classes);
        Intent chatIntent = new Intent(this, FriendActivity.class);
        Intent classesIntent = new Intent(this, DepartmentsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);

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
        String attendance = getIntent().getStringExtra("attendance");
        String comments = getIntent().getStringExtra("comments");
        String workload = getIntent().getStringExtra("workload");
        Integer score = getIntent().getIntExtra("rating", 0);
        Integer profRat = getIntent().getIntExtra("prof", 0);
        String late = getIntent().getStringExtra("late");
        String selected_course = getIntent().getStringExtra("selectedCourse");
        String department = getIntent().getStringExtra("department");
        String uid = getIntent().getStringExtra("uid");
        Integer up_count = 0;
        Integer down_count = 0;
        Review review = new Review(attendance, comments, workload, score, profRat, late, up_count, down_count);
        // populate form
        courseEditText = findViewById(R.id.edit_text_course);
        courseEditText.setRating(score);
        profRating = findViewById(R.id.ratingProf);
        profRating.setRating(profRat);
        EditText commentsEditText = findViewById(R.id.edit_text_comments);
        commentsEditText.setText(comments);
        EditText attendancEditText = findViewById(R.id.attendanceText);
        attendancEditText.setText(attendance);
        EditText workloadEdit = findViewById(R.id.workloadText);
        workloadEdit.setText(workload);
        EditText lateEdit = findViewById(R.id.lateText);
        lateEdit.setText(late);

        //update form
        submitButton = findViewById(R.id.submit_review_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newRating = (int) courseEditText.getRating();
                newProf = (int) profRating.getRating();
                String newComments = String.valueOf(commentsEditText.getText());
                String newWorkload = String.valueOf(workloadEdit.getText());
                String newAttendance = String.valueOf(attendancEditText.getText());
                String newLate = String.valueOf(lateEdit.getText());
                Review review = new Review(newAttendance, newComments, newWorkload, newRating, newProf,newLate,0,0);
                base = FirebaseDatabase.getInstance();




                DatabaseReference reviewref = base.getReference("departments").child(department).child("courses").child(selected_course).child("reviews");
                reviewref.child(uid).setValue(review);
                Toast.makeText(EditReview.this,"Review Updated",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditReview.this, DepartmentsActivity.class);
                startActivity(intent);

            }
        });

    }
}