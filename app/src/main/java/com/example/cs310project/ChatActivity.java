package com.example.cs310project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter;
    private Message message;
    private String sender, receiver, first, second;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
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

        recyclerView = findViewById(R.id.recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("messages");
        mAuth = FirebaseAuth.getInstance();
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (intent != null) {
            receiver = intent.getStringExtra("selectedStudent");
        }
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        sender = firebaseUser.getUid();

        int compareResult = sender.compareTo(receiver);
        if (compareResult < 0) {
            // sender comes before receiver alphabetically
            first = sender;
            second = receiver;
        } else if (compareResult > 0) {
            // receiver comes before sender alphabetically
            first = receiver;
            second = sender;
        }

        fetchMessages(sender);

        sendButton.setOnClickListener(view -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText, sender, receiver);
                messageInput.setText("");
            }
        });
    }

    private void sendMessage(String message, String sender, String receiver) {
        String key = databaseReference.push().getKey();
        key = first+"&"+second+"&"+key;
        Message newMessage = new Message(message, sender, receiver); // Use this.sender and this.receiver
        newMessage.setText(message);
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        if (key != null) {
            databaseReference.child(key).setValue(newMessage);
        }
    }
    private void fetchMessages(String currentUser) {

        Query query = databaseReference.orderByKey().startAt(first + "&" + second);
        FirebaseRecyclerOptions<Message> messagesOptions = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        FirebaseRecyclerAdapter<Message, MessageViewHolder> messagesAdapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(messagesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                if (model.getSender().equals(currentUser)) {
                    // Set sent messages
                    holder.setMessage("sent", model.getText());
                } else if (model.getReceiver().equals(currentUser)) {
                    // Set received messages
                    holder.setMessage("received", model.getText());
                }
            }

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new MessageViewHolder(view);
            }
        };

        recyclerView.setAdapter(messagesAdapter);
        messagesAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.text_message);
        }

        public void setMessage(String type, String message) {

            messageTextView.setText(type + ": " + message);

        }
    }
}
