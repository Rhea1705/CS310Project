package com.example.cs310project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    String currUser, currReceiver;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.chat);
        Intent chatIntent = new Intent(this, ChatActivity.class);
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

        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database reference
        databaseReference = firebaseDatabase.getReference().child("messages");
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        currUser = firebaseUser.getUid();

        Intent intent = getIntent();
        if (intent != null) {
            currReceiver = intent.getStringExtra("selectedStudent");
        }
        getFriends(currUser, databaseReference);
    }

    private void getFriends(String uuid, DatabaseReference databaseReference) {
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
                                displayFriend(names[1]);
                            }
                        } else {
                            if (!friendKeys.contains(names[0])) {
                                friendKeys.add(names[0]);
                                Log.d("friend", names[0]);
                                displayFriend(names[0]);
                            }
                        }
                    }
                    displayFriend(uuid);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void displayFriend(String userID) {
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout friendsList = findViewById(R.id.friendsList);
        View friendItem = inflater.inflate(R.layout.friend_item, friendsList, false);
        TextView studentName = friendItem.findViewById(R.id.name);
        studentName.setText(userID);
        userReference = firebaseDatabase.getReference().child("UserList").child(currUser).child("blocked");
        studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FriendActivity.this, ChatActivity.class);
                intent.putExtra("selectedID", userID);
                startActivity(intent);
            }
        });
        Button blockBtn = friendItem.findViewById(R.id.blockBtn);
        blockBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //block func : rhea
                if(blockBtn.getText() == "Block") {
                    blockBtn.setText("Unblocked");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.yellow));
                    userReference.child(currReceiver);
                }
                else {
                    blockBtn.setText("Block");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.crimson));
                    userReference.child(currReceiver);
                    userReference.removeValue()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
            }
        });
    }

}
