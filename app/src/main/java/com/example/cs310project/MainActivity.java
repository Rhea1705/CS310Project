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


import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();


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
                if(!email.endsWith("usc.edu")){
                    Toast.makeText(MainActivity.this,"Please use an usc.edu email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(usc.length() !=10){
                    Toast.makeText(MainActivity.this,"USC ID number can only contain 10 digits",Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                        MainActivity.this,task -> {
                            if(task.isSuccessful()){
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                DatabaseReference childref = reference.child("UserList");
                                String uuid = firebaseUser.getUid();
                                DatabaseReference useref = childref.child(uuid);
                                useref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Toast.makeText(MainActivity.this,"User already exists",Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        User user = new User(name,email,password,phone_number,usc,role,username,uuid);
                                        useref.setValue(user);
                                        Toast.makeText(MainActivity.this,"You have successfully created an account",Toast.LENGTH_SHORT).show();
                                        Access.username = uuid;
                                        Intent intent = new Intent(MainActivity.this,DepartmentsActivity.class);
                                        startActivity(intent);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(MainActivity.this,"Error while creating account" + task.getException(),Toast.LENGTH_SHORT).show();
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
