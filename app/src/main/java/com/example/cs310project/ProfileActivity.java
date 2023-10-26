
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

public class ProfileActivity extends AppCompatActivity {
    EditText image;
    Button Sign_up;
    Button Profile_update;
    FirebaseDatabase base;
    StorageReference storageRef;
    Uri imageUri;
    DatabaseReference reference;
    DatabaseReference imagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                // error
                Toast.makeText(ProfileActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}