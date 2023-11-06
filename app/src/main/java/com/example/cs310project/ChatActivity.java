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
import android.widget.Button;
import android.widget.EditText;

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
    private ArrayList<String> keysWithFirst = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recycler_view);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);

        // Initialize Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("messages");

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inside your activity or fragment:
        Access access = new Access();
        sender = "bulbul";
        receiver = "boots";
        access.username = "bulbul";

        // Fetch messages based on the sender or receiver

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
        getFriends(sender);
//        fetchMessages(sender);

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
    private void getFriends(String me){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    if (key.contains(me)) {
                        String[] names = key.split("&");
                        if(names[0] == me){
                            if(!keysWithFirst.contains(names[1])){
                                keysWithFirst.add(names[1]);
                                Log.d("friend", names[1]);
                            }
                        }
                        else{
                            if(!keysWithFirst.contains(names[0])){
                                keysWithFirst.add(names[0]);
                                Log.d("friend", names[0]);
                            }
                        }
                    }
                }
                fetchMessages(me);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message, parent, false);
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
