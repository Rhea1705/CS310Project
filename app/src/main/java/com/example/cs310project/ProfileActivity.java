
package com.example.cs310project;

import
        androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.StorageReference;

//public class ProfileActivity extends AppCompatActivity {
//    EditText image;
//    Button Sign_up;
//    Button Profile_update;
//    FirebaseDatabase base;
//    Uri imageUri;
//    DatabaseReference reference;
//    DatabaseReference imagesRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        // Initialize Firebase Database
//        base = FirebaseDatabase.getInstance();
//        reference = base.getReference("UserList"); // Replace with your desired database node
//        imagesRef = base.getReference("images");
//
//        image = findViewById(R.id.idEdtUserName); //replace with image id
//
//        Profile_update = findViewById(R.id.updateProfileButton); //replace with correct button id
//
//        // Retrieve user information and populate the EditText fields
//        DatabaseReference userRef = reference.child("userId"); // Replace "userId" with the unique user identifier
//        Profile_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Read the image URL from the EditText (assuming it's a URL)
//                String imageUrl = image.getText().toString();
//
//                if (!imageUrl.isEmpty()) {
//                    uploadProfilePic(imageUrl);
//                }
//            }
//        });
//    }
//
//    private void uploadProfilePic(String imageUrl) {
//        // Create a new image entry with a unique ID
//        String imageId = imagesRef.push().getKey();
//
//        // Create an ImageInfo object
//        Image imageInfo = new Image(imageUrl);
//
//        // Save the image data to the database under the unique ID
//        imagesRef.child(imageId).setValue(imageInfo);
//    }
//    private void listenForImageChanges() {
//        imagesRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
//                    Image imageInfo = imageSnapshot.getValue(Image.class);
//                    if (imageInfo != null) {
//                        // Do something with the image data, e.g., display it in your app
//                        // You can access imageInfo.getImageUrl()
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // error
//                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}
public class ProfileActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText, emailEditText, phoneEditText, imageEditText, roleEditText, idEditText;
    Button updateButton;
    FirebaseDatabase database;
    DatabaseReference userReference;
    DatabaseReference imagesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("UserList");
        imagesReference = database.getReference("images");

//      Gauri: these ids can be used when writing activity_profile.xml under res/layout
//        usernameEditText = findViewById(R.id.usernameEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        emailEditText = findViewById(R.id.emailEditText);
//        phoneEditText = findViewById(R.id.phoneEditText);
//        imageEditText = findViewById(R.id.imageEditText);
//        roleEditText = findViewById(R.id.roleEditText);
//        idEditText = findViewById(R.id.idEditText);
//        updateButton = findViewById(R.id.updateButton);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read user data and update it in the database
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone_number = phoneEditText.getText().toString();
                String imageUrl = imageEditText.getText().toString();
                String role = roleEditText.getText().toString();
                String id = idEditText.getText().toString();

                // Save or update user data in the database
                updateUserData(username, password, email,phone_number, imageUrl, role, id);
            }
        });

        // Listen for changes in user data and update the UI
        listenForUserDataChanges();
    }

    private void updateUserData(String username, String password, String email, String phone_number, String imageUrl, String role, String id) {
        // Create a new user entry with a unique ID
        String userId = userReference.push().getKey();

        // Create a UserData object
        UserData userData = new UserData(username, password, email, phone_number, imageUrl, role, id);

        // Save the user data to the database under the unique ID
        userReference.child(userId).setValue(userData);
    }

    private void listenForUserDataChanges() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    UserData userData = userSnapshot.getValue(UserData.class);
                    if (userData != null) {
                        // Update the UI with user data
//                       To do: check username isn't taken
                        if(userData.getUsername()!=null){
                            usernameEditText.setText(userData.getUsername());
                        }
                        if(userData.getPassword()!=null){
                            passwordEditText.setText(userData.getPassword());
                        }
                        if(userData.getEmail()!=null) {
                            emailEditText.setText(userData.getEmail());
                        }
                        if(userData.getImageUrl()!=null) {
                            imageEditText.setText(userData.getImageUrl());
                        }
                        if(userData.getPhoneNumber()!=null) {
                            phoneEditText.setText(userData.getPhoneNumber());
                        }
//                       To do: set options to choose from???
                        if(userData.getRole()!=null) {
                            roleEditText.setText(userData.getRole());
                        }
//                        check for length to be 10 digits
                        if(userData.getUSCid()!=null) {
                            if (userData.getUSCid().length() != 10) {
                                // Show an error message if the ID is not 10 digits
                                idEditText.setError("ID must be 10 digits");
                            }
                            else {
                                idEditText.setText(userData.getUSCid());
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
