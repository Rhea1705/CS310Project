package com.example.cs310project;

import androidx.annotation.NonNull;
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
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    EditText namefield,usernamefield,passwordfield,emailfield,phone_numberfield, image;
    Button Sign_up;
    Button Profile_update;
    FirebaseDatabase base;
    StorageReference storageRef;
    Uri imageUri;
    DatabaseReference reference;
    DatabaseReference imagesRef;

    protected void ImageUpload(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database
        base = FirebaseDatabase.getInstance();
        reference = base.getReference("UserList"); // Replace with your desired database node
        imagesRef = base.getReference("images");

        image = findViewById(R.id.idEdtUserName); //replace with image id

        Profile_update = findViewById(R.id.idBtnRegister); //replace with correct button id
        Profile_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Read the image URL from the EditText (assuming it's a URL)
                String imageUrl = image.getText().toString();

                if (!imageUrl.isEmpty()) {
                    uploadProfilePic(imageUrl);
                }
            }
        });
    }

    private void uploadProfilePic(String imageUrl) {
        // Create a new image entry with a unique ID
        String imageId = imagesRef.push().getKey();

        // Create an ImageInfo object
        Image imageInfo = new Image(imageUrl);

        // Save the image data to the database under the unique ID
        imagesRef.child(imageId).setValue(imageInfo);
    }

    // You can listen for changes in the images node
    private void listenForImageChanges() {
        imagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    Image imageInfo = imageSnapshot.getValue(Image.class);
                    if (imageInfo != null) {
                        // Do something with the image data, e.g., display it in your app
                        // You can access imageInfo.getImageUrl()
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(MainActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namefield = findViewById(R.id.name);
        usernamefield =  findViewById(R.id.idEdtUserName);
        passwordfield =  findViewById(R.id.idEdtPassword);
        emailfield = findViewById(R.id.email);
        phone_numberfield = findViewById(R.id.phone);
       Sign_up = findViewById(R.id.idBtnRegister);
       base = FirebaseDatabase.getInstance();
       reference = base.getReference();

       Sign_up.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
            String name = namefield.getText().toString();
            String username = usernamefield.getText().toString();
            String password = passwordfield.getText().toString();
            String email = emailfield.getText().toString();
            String phone_number = phone_numberfield.getText().toString();
            User user = new User(name,email,password,phone_number);
            DatabaseReference childref = reference.child("UserList");
            DatabaseReference usename = childref.child(username);
            usename.child("name").setValue(name);
            usename.child("password").setValue(password);
            usename.child("email").setValue(email);
            usename.child("phone_number").setValue(phone_number);
            Toast.makeText(MainActivity.this,"You have successfully created an account",Toast.LENGTH_SHORT).show();


           }
       });
    }
}