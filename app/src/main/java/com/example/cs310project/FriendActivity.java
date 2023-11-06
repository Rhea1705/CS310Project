package com.example.cs310project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
//        setContentView(R.layout.activity_friend_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("messages");

        getFriends("bulbul", databaseReference);
    }

    private void getFriends(String me, DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    if (key.contains(me)) {
                        String[] names = key.split("&");
                        if (names[0].equals(me)) {
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
                }
                displayFriends();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors
            }
        });
    }

    private void displayFriends() {

    }

}
