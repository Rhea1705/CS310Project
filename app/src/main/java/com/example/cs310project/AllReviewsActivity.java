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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AllReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
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
        String selectedCourse= getIntent().getStringExtra("selectedCourse");
        String department = getIntent().getStringExtra("department");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reviewsRef = database.getReference("departments").child(department).child("courses").child(selectedCourse).child("reviews");

        // Reference to the LinearLayout container
        LinearLayout reviewListLayout = findViewById(R.id.reviewListLayout);
        LinearLayout buttonLayout = findViewById(R.id.addBtnLayout);
        reviewsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewListLayout.removeAllViews();
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                    String attendance = courseSnapshot.child("attendance").getValue(String.class);
                    String comments = courseSnapshot.child("comments").getValue(String.class);
                    String workload = courseSnapshot.child("workload").getValue(String.class);
                    Integer rating = courseSnapshot.child("score").getValue(Integer.class);
                    Integer profRating = courseSnapshot.child("prof").getValue(Integer.class);
                    String late = courseSnapshot.child("late").getValue(String.class);
                    String userID = courseSnapshot.child("userID").getValue(String.class);


//                    Log.d("course list", "course name: " + courseName);

                    createReviewItem(reviewListLayout,rating,workload,attendance, late, comments, profRating, userID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
        Button AddBtn = buttonLayout.findViewById(R.id.addReviewBtn);
        AddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
//                Log.d("Allreviws","on click: " + departmentName);
                Intent intent = new Intent(AllReviewsActivity.this, ReviewActivity.class);
                intent.putExtra("selectedCourse", selectedCourse);
                intent.putExtra("department", department);
                startActivity(intent);
                startActivity(intent);
            }
        });

    }
    private void createReviewItem(LinearLayout parentLayout, Integer rating, String workload, String attendance, String late, String comments, Integer prof, String userID) {
        LayoutInflater inflater = getLayoutInflater();
        View reviewItemView = inflater.inflate(R.layout.review_item, parentLayout, false);

        TextView courseRating = reviewItemView.findViewById(R.id.courseRating);

        courseRating.setText(rating.toString());
        TextView workloadText = reviewItemView.findViewById(R.id.worloadText);
        workloadText.setText(workload);
        TextView attText = reviewItemView.findViewById(R.id.attendanceText);
        attText.setText(attendance);
        TextView lateText = reviewItemView.findViewById(R.id.hwText);
        lateText.setText(late);
        TextView otherText = reviewItemView.findViewById(R.id.otherText);
        otherText.setText(comments);
        TextView profRating = reviewItemView.findViewById(R.id.profRating);
        profRating.setText(prof.toString());

        TextView likeCount = reviewItemView.findViewById(R.id.likeCount);
        TextView dislikeCount = reviewItemView.findViewById(R.id.dislikeCount);
        ImageView likeBtn = reviewItemView.findViewById(R.id.likeBtn);
        ImageView dislikeBtn = reviewItemView.findViewById(R.id.dislikeBtn);
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeBtn.setImageResource(R.drawable.like_selected);
                //vidit update like count
            }
        });
        dislikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeBtn.setImageResource(R.drawable.dislike_selected);
                //vidit update dislike count
            }
        });

        //how to convert user ID to student name ?? vidit
//        TextView nameText = reviewItemView.findViewById(R.id.nameText);
//        nameText.setText(userID);


        parentLayout.addView(reviewItemView);
    }
}