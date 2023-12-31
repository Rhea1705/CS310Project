package com.example.cs310project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
    private ArrayList<String> friendKeys = new ArrayList<>();
    private DatabaseReference databaseReference;
    private DatabaseReference userReference;
    FirebaseDatabase firebaseDatabase;
    String currUser, currReceiver, friend, name;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.chat);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database reference
        databaseReference = firebaseDatabase.getReference().child("messages");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        currUser = firebaseUser.getUid();

        getFriends(currUser, databaseReference);
    }

    private void getFriends(String uuid, DatabaseReference databaseReference) {
        DatabaseReference tempRef = firebaseDatabase.getReference().child("UserList");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    if (key.contains(uuid)) {
                        String[] names = key.split("&");
                        if (names[0].equals(uuid)) {
                            if (!friendKeys.contains(names[1])) {
                                friendKeys.add(names[1]);
                                Log.d("friend", names[1]);
                                // Assume tempRef is your DatabaseReference
                                name = names[1];
                                currReceiver = names[1];
                                fetchFriendDetails(name);
                            }
                        } else {
                            if (!friendKeys.contains(names[0])) {
                                friendKeys.add(names[0]);
                                Log.d("friend", names[0]);
                                name= names[0];
                                currReceiver = names[0];
                                fetchFriendDetails(name);
                            }
                        }
                        // Assume tempRef is your DatabaseReferenc


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }
    // Add a method to fetch friend details
    private void fetchFriendDetails(String name) {
        DatabaseReference tempRef = firebaseDatabase.getReference().child("UserList").child(name).child("name");
        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String friend = dataSnapshot.getValue(String.class);
                displayFriend(friend, name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
                Log.e("FirebaseData", "Error fetching 'name' field: " + databaseError.getMessage());
            }
        });
    }

    private void displayFriend(String friend, String friendUid) {
        Log.d("FriendActivity", "Friend name: " + friend + "  " + friendUid);
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout friendsList = findViewById(R.id.friendsList);

        View friendItem = inflater.inflate(R.layout.friend_item, friendsList, false);
        TextView studentName = friendItem.findViewById(R.id.name);
        studentName.setText(friend);

        userReference = firebaseDatabase.getReference().child("UserList").child(currUser).child("blocked");
        studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("adding", friendUid);
                Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
                intent.putExtra("selectedStudent", friendUid);
                startActivity(intent);
            }
        });
        Button blockBtn = friendItem.findViewById(R.id.blockBtn);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the particular child exists
                if (dataSnapshot.hasChild(friendUid)) {
                    blockBtn.setText("Unblocked");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.yellow));
                } else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
        blockBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //block func : rhea
                if(blockBtn.getText() == "Block") {
                    blockBtn.setText("Unblocked");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.yellow));
                    userReference.child(currReceiver).setValue(true);
                }
                else {
                    blockBtn.setText("Block");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.crimson));
                    userReference.child(currReceiver).removeValue()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });
        Button profileBtn = friendItem.findViewById(R.id.profileBtn);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the courses activity with the selected department
                Intent intent = new Intent(FriendActivity.this, DisplayProfileActivity.class);
                intent.putExtra("selectedUUID", friendUid);
                startActivity(intent);
            }
        });
        friendsList.addView(friendItem);
    }

}
