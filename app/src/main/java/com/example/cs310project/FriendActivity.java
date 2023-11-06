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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> keysWithFirst = new ArrayList<>();
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");

        getFriends("bulbul", databaseReference);
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
                            if (!keysWithFirst.contains(names[1])) {
                                keysWithFirst.add(names[1]);
                                Log.d("friend", names[1]);
                            }
                        } else {
                            if (!keysWithFirst.contains(names[0])) {
                                keysWithFirst.add(names[0]);
                                Log.d("friend", names[0]);
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
                }
                else {
                    blockBtn.setText("Block");
                    blockBtn.setBackgroundColor(ContextCompat.getColor(FriendActivity.this, R.color.crimson));
                }
            }
        });
    }

}
