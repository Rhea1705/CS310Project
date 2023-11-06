package com.example.cs310project;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DisplayProfileActivity extends AppCompatActivity {
    TextView nameText, usernameText, passwordText, emailText, phoneText, roleText, idText;
    ImageView imageView;
    FirebaseDatabase database;
    DatabaseReference userReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    String selectedUUID;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("UserList");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        // Views for profile data
        nameText = findViewById(R.id.pName);
        usernameText = findViewById(R.id.usernameEditText);
        passwordText = findViewById(R.id.passwordEditText);
        emailText = findViewById(R.id.emailEditText);
        phoneText = findViewById(R.id.phoneEditText);

        imageView = findViewById(R.id.imageEditText);

        roleText = findViewById(R.id.roleEditText);
        idText = findViewById(R.id.pUSCid);

        Intent intent = getIntent();
        if (intent != null) {
            selectedUUID = intent.getStringExtra("selectedUUID");
        }
        loadPage();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
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


        // Initialize the ActivityResultLauncher here
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        filePath = result.getData().getData();
                        Glide.with(this).load(filePath).into(imageView);
                    }
                }
        );
    }
    private void loadPage() {
        String currentUser = selectedUUID;

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("UserList").child(currentUser);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if(user.getName() != null){
                        nameText.setText(user.getName());
                    }
                    if (user.getUsername() != null) {
                        usernameText.setText(user.getUsername());
                    }
                    if (user.getPassword() != null) {
                        passwordText.setText(user.getPassword());
                    }
                    if (user.getUsc_id() != null) {
                        idText.setText(user.getUsc_id());
                    }
                    if (user.getEmail() != null) {
                        emailText.setText(user.getEmail());
                    }
                    if (user.getPhone_number() != null) {
                        phoneText.setText(user.getPhone_number());
                    }
                    if (user.getRole() != null) {
                        roleText.setText(user.getRole());
                    }
                    // Load the profile image into ImageView
                    String imageUrl = "/images/" + selectedUUID; // Check the correct path in Firebase Storage
                    StorageReference imageRef = storage.getReference(imageUrl);

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(DisplayProfileActivity.this)
                                .load(uri)
                                .into(imageView);
                    }).addOnFailureListener(e -> {
                        // Handle any errors while fetching the image
//                        Toast.makeText(ProfileActivity.this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DisplayProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

