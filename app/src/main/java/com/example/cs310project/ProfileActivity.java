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

public class ProfileActivity extends AppCompatActivity {
    EditText nameEditText, usernameEditText, passwordEditText, emailEditText, phoneEditText, roleEditText, idEditText;
    ImageView imageView;
    Button updateButton, imgButton;
    FirebaseDatabase database;
    DatabaseReference userReference;
    StorageReference imagesReference;
    private Uri filePath;
    private FirebaseStorage storage;
    StorageReference storageReference;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    FirebaseAuth mAuth;
    String storageName, name, username, password, id, email, phone_number, role;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("UserList");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mAuth = FirebaseAuth.getInstance();

        // Views for profile data
        nameEditText = findViewById(R.id.pName);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);

        imageView = findViewById(R.id.imageEditText);

        roleEditText = findViewById(R.id.roleEditText);
        idEditText = findViewById(R.id.pUSCid);
        updateButton = findViewById(R.id.updateButton);
        imgButton = findViewById(R.id.imgButton);



        loadPage();
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        Intent chatIntent = new Intent(this, FriendActivity.class);
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
        // Need to add button in layout file
        imgButton.setOnClickListener(view -> SelectImage());

        updateButton.setOnClickListener(view -> {
            // Read user data and update it in the database
            name = nameEditText.getText().toString();
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            email = emailEditText.getText().toString();
            phone_number = phoneEditText.getText().toString();
            role = roleEditText.getText().toString();
            id = idEditText.getText().toString();
            // Save or update user data in the database
            updateUserData(name, username, password, email, id, phone_number, role);
//            updatePage(name, username, password, id, email, phone_number, storageName, role);
        });
    }
    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent); // Use the initialized launcher to start activity for result
//        updateUserData(name, username, password, email, id, phone_number, role);
    }
    private void updateUserData(String name, String username, String password, String id, String email, String phone_number, String role) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uuid = firebaseUser.getUid();

        storageName = "images/"+uuid;
        imagesReference = storage.getReference(storageName);
        if(filePath!=null) {
            imagesReference.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(ProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        updatePage(name, username, password, id, email, phone_number, null, role);
    }

    private void updatePage(String name, String username, String password, String id, String email, String phone_number, String image, String role) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uuid = firebaseUser.getUid();

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("UserList").child(uuid);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (name != null) {
                        user.setName(name);
                    }
                    if (username != null) {
                        user.setUsername(username);
                    }
                    if (password != null) {
                        user.setPassword(password);
                    }
                    if (id != null) {
                        if (id.length() != 10) {
                            Toast.makeText(ProfileActivity.this, "id length should be 10 digits ", Toast.LENGTH_SHORT).show();
                        } else {
                            user.setUsc_id(id);
                        }
                    }
                    if (email != null) {
                        user.setEmail(email);
                    }
                    if (phone_number != null) {
                        user.setPhone_number(phone_number);
                    }
                    if (role != null) {
                        user.setRole(role);
                    }
                    if (image != null) {
                        user.setImageUrl(image);
                    }

                    // Update the user data in the Firebase Database
                    userReference.setValue(user)
                            .addOnSuccessListener(aVoid -> {
                                // Handle successful update
                                Toast.makeText(ProfileActivity.this, "User data updated", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle update failure
                                Toast.makeText(ProfileActivity.this, "Failed to update user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPage() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uuid = firebaseUser.getUid();
        String currentUser = uuid;

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("UserList").child(currentUser);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if(user.getName() != null){
                        nameEditText.setText(user.getName());
                    }
                    if (user.getUsername() != null) {
                        usernameEditText.setText(user.getUsername());
                    }
                    if (user.getPassword() != null) {
                        passwordEditText.setText(user.getPassword());
                    }
                    if (user.getUsc_id() != null) {
                        idEditText.setText(user.getUsc_id());
                    }
                    if (user.getEmail() != null) {
                        emailEditText.setText(user.getEmail());
                    }
                    if (user.getPhone_number() != null) {
                        phoneEditText.setText(user.getPhone_number());
                    }
                    if (user.getRole() != null) {
                        roleEditText.setText(user.getRole());
                    }

                    // Load the profile image into ImageView
                    String imageUrl = "/images/" + uuid; // Check the correct path in Firebase Storage
                    StorageReference imageRef = storage.getReference(imageUrl);

                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Glide.with(ProfileActivity.this)
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
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

