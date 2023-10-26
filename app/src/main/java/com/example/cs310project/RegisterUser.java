package com.example.cs310project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;

public class RegisterUser extends AppCompatActivity {
        EditText name,username,password,email,phone_number;
        Button Sign_up;
        Firebase base;
        DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);



    }
}