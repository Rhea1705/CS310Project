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
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private Button sendButton;
    private DatabaseReference databaseReference, blockReference;
    FirebaseDatabase firebaseDatabase;
    private FirebaseRecyclerAdapter<Message, MessageViewHolder> adapter;
    private Message message;
    private String sender, receiver, first, second;
    FirebaseAuth mAuth;
    String key;
    ChatHelper chatHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.chat);
        Intent chatIntent = new Intent(this, FriendActivity.class);
        Intent classesIntent = new Intent(this, DepartmentsActivity.class);
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        chatHelper = new ChatHelper();
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

        recyclerView = findViewById(R.id.recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
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

//        sender = "KTI9LhzrjLXp0mfztVeyqOwUPlA3";
//        receiver = "nd4IdLHHURbkk4gVPinLbYVIeKb2";
        setFirstAndSecond(chatHelper.getFirst(sender, receiver), chatHelper.getSecond(sender, receiver));
//        int compareResult = sender.compareTo(receiver);
//        if (compareResult < 0) {
//            // sender comes before receiver alphabetically
//            first = sender;
//            second = receiver;
//        } else if (compareResult > 0) {
//            // receiver comes before sender alphabetically
//            first = receiver;
//            second = sender;
//        }

        fetchMessages(sender);

        sendButton.setOnClickListener(view -> {
            String messageText = messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText, sender, receiver);
                messageInput.setText("");
            }
        });
    }
    public void setFirstAndSecond(String firstValue, String secondValue) {
        this.first = firstValue;
        this.second = secondValue;
    }
    void sendMessage(String message, String sender, String receiver) {
        key = databaseReference.push().getKey();
        key = first+"&"+second+"&"+key;

        blockReference = firebaseDatabase.getReference().child("UserList").child(receiver).child("blocked");
        // Assuming blockReference is a DatabaseReference object
        blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Check if the particular child exists
                if (dataSnapshot.hasChild(sender)) {
                    Toast.makeText(ChatActivity.this, "cannot send message to this user ", Toast.LENGTH_SHORT).show();
                } else {
                    Message newMessage = new Message(message, sender, receiver); // Use this.sender and this.receiver
                    newMessage.setText(message);
                    newMessage.setSender(sender);
                    newMessage.setReceiver(receiver);
                    if (chatHelper.checkNull(key)) {
//                        databaseReference.child(key).setValue(newMessage);
                        sendMessageTo(newMessage);
                    }
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
    boolean sendMessageTo(Message newMessage){
        databaseReference.child(key).setValue(newMessage);
        return true;
    }
    private void fetchMessages(String currentUser) {
        boolean view = false;
        Log.d("first", first);
        Log.d("second", second);
        Query query = databaseReference.orderByKey().startAt(first + "&" + second);
        FirebaseRecyclerOptions<Message> messagesOptions = new FirebaseRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Message, MessageViewHolder>(messagesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                if (model.getSender().equals(currentUser)) {

                    // Set sent messages
                    if (model.getReceiver().equals(receiver)){
                        Log.d("model.getText() sender", model.getText());
                        holder.setMessage("sent", model.getText());
                    }
                } else if (model.getReceiver().equals(currentUser)) {

                    // Set received messages
                    if (model.getSender().equals(receiver)){
                        Log.d("model.getText() receiver", model.getText());
                        holder.setMessage("received", model.getText());
                    }
                }
            }


            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
                return new MessageViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
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
            if(type !=null && message != null) {
                Log.d("type", type);
                Log.d("message", message);
                messageTextView.setText(type + ": " + message);
            }

        }
    }
}
