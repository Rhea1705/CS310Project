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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    EditText usernameEditText, passwordEditText, emailEditText, phoneEditText, imageEditText, roleEditText, idEditText;
    ImageView imageView;
    Button updateButton, imgButton;
    FirebaseDatabase database;
    DatabaseReference userReference;
    StorageReference imagesReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseStorage storage;
    StorageReference storageReference;
    ProgressBar progressBar;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("UserList");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        DatabaseReference databaseReference = database.getReference();
        DatabaseReference getImage = databaseReference.child("image");

        // Views for profile data
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
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone_number = phoneEditText.getText().toString();
            String imageUrl = imageEditText.getText().toString();
            String role = roleEditText.getText().toString();
            String id = idEditText.getText().toString();

            // Save or update user data in the database
            updateUserData(username, password, email, phone_number, imageUrl, role, id);
        });
    }
    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent); // Use the initialized launcher to start activity for result
    }
    private void updateUserData(String username, String password, String email, String phone_number, String image, String role, String id) {
    //    progressBar = new ProgressBar(this);
    //    progressBar.isShown();
        Access access =  new Access();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
        Date now = new Date();
        String storageName = "images/"+access.username;
        imagesReference = storage.getReference(storageName);

        imagesReference.putFile(filePath)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(ProfileActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                    updatePage(username, password, email, phone_number, storageName, role, id);
    //                if(progressBar.isShown()){
    //                    progressBar.dismiss();
    //                }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        updatePage(username, password, email, phone_number, image, role, id);
    }

    private void updatePage(String username, String password, String email, String phone_number, String image, String role, String id) {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserData userData = userSnapshot.getValue(UserData.class);
                    if (userData != null) {
                        if (username != null) {
                            userData.setUsername(phone_number);
                        }
                        if(password!=null){
                            userData.setPassword(phone_number);
                        }
                        if(email!=null) {
                            userData.setEmail(phone_number);
                        }
                        if(phone_number!=null) {
                            userData.setPhoneNumber(phone_number);
                        }
                        if (image != null) {
                            userData.setImageUrl(image);
                        }
//                       To do: set options to choose from???
                        if(role!=null) {
                            userData.setRole(role);
                        }
//                        check for length to be 10 digits
                        if(id!=null) {
                            if (id.length() != 10 && id.length() > 0) {
                                // Show an error message if the ID is not 10 digits
                                idEditText.setError("ID must be 10 digits");
                            }
                            else {
                                userData.setUSCid(id);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadPage() {
        Access access = new Access();
        String currentUser = access.username;
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("UserList").child(currentUser);

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserData userData = dataSnapshot.getValue(UserData.class);
                if (userData != null) {
                    if (userData.getUsername() != null) {
                        usernameEditText.setText(userData.getUsername());
                    }
                    if (userData.getPassword() != null) {
                        passwordEditText.setText(userData.getPassword());
                    }
                    if (userData.getEmail() != null) {
                        emailEditText.setText(userData.getEmail());
                    }
                    if (userData.getPhoneNumber() != null) {
                        phoneEditText.setText(userData.getPhoneNumber());
                    }
                    if (userData.getImageUrl() != null) {
                        // Load the profile image into ImageView
                        String imageUrl = userData.getImageUrl();
                        Picasso.get().load(imageUrl).into(imageView);
                    }
//                       To do: set options to choose from???
                    if (userData.getRole() != null) {
                        roleEditText.setText(userData.getRole());
                    }
//                        check for length to be 10 digits
                    if (userData.getUSCid() != null) {
                        idEditText.setText(userData.getUSCid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

