package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;


import com.example.csci310finalproject.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText namefield,usernamefield,passwordfield,emailfield,phone_numberfield,usc_id;
    Spinner spin;
    Button Sign_up;
    FirebaseDatabase base;
    DatabaseReference reference;
    TextView login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        namefield = findViewById(R.id.name);
        usernamefield =  findViewById(R.id.UserName);
        passwordfield =  findViewById(R.id.Password);
        emailfield = findViewById(R.id.email);
        phone_numberfield = findViewById(R.id.phone);
        spin = findViewById((R.id.roleSpinner));
        usc_id = findViewById(R.id.USCid);
        login = findViewById(R.id.logInNav);


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
            String role = spin.getSelectedItem().toString();
            String usc = usc_id.getText().toString();
            DatabaseReference childref = reference.child("UserList");
            DatabaseReference usename = childref.child(username);
            usename.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Toast.makeText(MainActivity.this,"Username already exists",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User user = new User(name,email,password,phone_number,usc,role);
                    usename.setValue(user);
                    Toast.makeText(MainActivity.this,"You have successfully created an account",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,LogIn.class);
                    startActivity(intent);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



           }
       });
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this,LogIn.class);
               startActivity(intent);
           }
       });


    }
}