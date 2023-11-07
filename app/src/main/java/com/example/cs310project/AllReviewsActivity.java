package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllReviewsActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userref;
    FirebaseAuth mAuth;
    boolean removereview;
    boolean ilike;
    boolean idislike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
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
        database = FirebaseDatabase.getInstance();
        userref = database.getReference("UserList");
        DatabaseReference reviewsRef = database.getReference("departments").child(department).child("courses").child(selectedCourse).child("reviews");
        mAuth = FirebaseAuth.getInstance();
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
                    Integer profRating = courseSnapshot.child("profRating").getValue(Integer.class);
                    String late = courseSnapshot.child("late").getValue(String.class);
                    String uid = courseSnapshot.getKey();
                    Integer up_count = courseSnapshot.child("up_count").getValue(Integer.class);
                    Integer down_count = courseSnapshot.child("down_count").getValue(Integer.class);
                    Review review = new Review(attendance,comments,workload,rating,profRating,late,up_count,down_count);

                    boolean like;
                    if(courseSnapshot.child("users_who_liked").hasChild(mAuth.getCurrentUser().getUid())){
                        like = true;
                    }
                    else{
                        like = false;
                    }
                    boolean dislike;
                    if(courseSnapshot.child("users_who_disliked").hasChild(mAuth.getCurrentUser().getUid())){
                        dislike = true;
                    }
                    else{
                        dislike = false;
                    }
                    Log.d("review_app", "like flag " + like);
                    Log.d("review_app", "dislike flag " + dislike);
                    boolean my_review = false;
                    if(uid.equals(mAuth.getCurrentUser().getUid())){
                        my_review = true;
                    }
                    Log.d("review_app", "myreview flag " + my_review);
                    Log.d("review_app", "uid flag " + uid);
                    Log.d("review_app", "uuid flag " + mAuth.getCurrentUser().getUid());



//                    Log.d("course list", "course name: " + courseName);

                    createReviewItem(reviewListLayout,rating,workload,attendance, late, comments, profRating, uid,reviewsRef,up_count,down_count,like,dislike, review,my_review,selectedCourse,department);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FirebaseError", error.getMessage());
            }
        });
        Button AddBtn = buttonLayout.findViewById(R.id.addReviewBtn);
        Button button = findViewById(R.id.addReviewBtn);
        if(AddBtn == null) {
            Log.d("add button", "didnt get button");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
//                Log.d("Allreviews","on click: " + departmentName);
                Intent intent = new Intent(AllReviewsActivity.this, ReviewActivity.class);
                intent.putExtra("selectedCourse", selectedCourse);
                intent.putExtra("department", department);
                startActivity(intent);
                startActivity(intent);
            }
        });

    }
    private void createReviewItem(LinearLayout parentLayout, Integer rating, String workload, String attendance, String late, String comments, Integer prof,
                                  String uid, DatabaseReference reviewsRef,Integer up_count,Integer down_count, boolean like, boolean dislike,
                                  Review review, boolean my_review,String selectedCourse, String department) {
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
        if(prof != null) {
            profRating.setText(prof.toString());
        }
        DatabaseReference specific_userref = userref.child(uid);
        specific_userref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    return;
                }
                String name = snapshot.child("name").getValue(String.class);
                TextView nameText = reviewItemView.findViewById(R.id.nameText);
                nameText.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button editview = reviewItemView.findViewById(R.id.editBtn);
        Button deletebutton = reviewItemView.findViewById(R.id.delBtn);

        TextView likeCount = reviewItemView.findViewById(R.id.likeCount);
        likeCount.setText(String.valueOf(review.getUp_count()));
        TextView dislikeCount = reviewItemView.findViewById(R.id.dislikeCount);
        dislikeCount.setText(String.valueOf(review.getDown_count()));
        ImageView likeBtn = reviewItemView.findViewById(R.id.likeBtn);
        ImageView dislikeBtn = reviewItemView.findViewById(R.id.dislikeBtn);
        Log.d("review_function", "myreview flag " + my_review);
        if(my_review){
            editview.setVisibility(View.VISIBLE);
            deletebutton.setVisibility(View.VISIBLE);
        }
        if(like){
            likeBtn.setImageResource(R.drawable.like_selected);
        }
        else{
            likeBtn.setImageResource(R.drawable.like);
        }
        if(dislike){
            dislikeBtn.setImageResource(R.drawable.dislike_selected);
        }
        else{
            dislikeBtn.setImageResource(R.drawable.dislike);
        }
        editview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllReviewsActivity.this, EditReview.class);
                intent.putExtra("selectedCourse", selectedCourse);
                intent.putExtra("department", department);
                intent.putExtra("rating", rating);
                intent.putExtra("workload", workload);
                intent.putExtra("attendance", attendance);
                intent.putExtra("late", late);
                intent.putExtra("comments", comments);
                intent.putExtra("prof", prof);
                intent.putExtra("uid", uid);
                startActivity(intent);

            }
        });
        removereview = false;
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewsRef.child(uid).removeValue();
                removereview = true;
                Intent intent = new Intent(AllReviewsActivity.this,AllReviewsActivity.class);
                startActivity(intent);
            }
        });
        ilike = false;
        idislike = false;
        if(!uid.equals(mAuth.getCurrentUser().getUid())){
            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!idislike){

                        //if unclicked
                        if (likeBtn.getDrawable().getConstantState().equals(ContextCompat.getDrawable(AllReviewsActivity.this, R.drawable.like).getConstantState())) {
                            likeBtn.setImageResource(R.drawable.like_selected);
                            int new_num = review.getUp_count() +1;
                            reviewsRef.child(uid).child("up_count").setValue(new_num);
                            likeCount.setText(String.valueOf(new_num));
                            reviewsRef.child(uid).child("users_who_liked").child(mAuth.getCurrentUser().getUid()).setValue("like");
                            Log.d("review_app", "set uid " + mAuth.getCurrentUser().getUid());
                            review.setUp_count(new_num);
                            ilike = true;
                        }
                        //if clicked
                        else {
                            likeBtn.setImageResource(R.drawable.like);
                            int new_num = review.getUp_count() -1 ;
                            reviewsRef.child(uid).child("up_count").setValue(new_num);
                            likeCount.setText(String.valueOf(new_num));
                            reviewsRef.child(uid).child("users_who_liked").child(mAuth.getCurrentUser().getUid()).removeValue();
                            Log.d("review_app", "uid " + mAuth.getCurrentUser().getUid());
                            review.setUp_count(new_num);
                            ilike = false;

                        }
                    }


                }
            });
            dislikeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!ilike){
                        if(dislikeBtn.getDrawable().getConstantState().equals(ContextCompat.getDrawable(AllReviewsActivity.this, R.drawable.dislike).getConstantState())){
                            dislikeBtn.setImageResource(R.drawable.dislike_selected);
                            //vidit update dislike count
                            int new_num = review.getDown_count() +1;
                            reviewsRef.child(uid).child("down_count").setValue(new_num);
                            dislikeCount.setText(String.valueOf(new_num));
                            reviewsRef.child(uid).child("users_who_disliked").child(mAuth.getCurrentUser().getUid()).setValue("dislike");
                            review.setDown_count(new_num);
                            idislike = true;

                        }
                        else{
                            dislikeBtn.setImageResource(R.drawable.dislike);
                            //vidit update dislike count
                            int new_num = review.getDown_count() -1;
                            reviewsRef.child(uid).child("down_count").setValue(new_num);
                            dislikeCount.setText(String.valueOf(new_num));
                            reviewsRef.child(uid).child("users_who_disliked").child(mAuth.getCurrentUser().getUid()).removeValue();
                            review.setDown_count(new_num);
                            idislike = false;
                        }
                    }


                }



            });
        }



        if(!removereview){
            parentLayout.addView(reviewItemView);
        }
    }
}