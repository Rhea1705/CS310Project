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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    EditText usernamefield,passwordfield;
    Button login;
    FirebaseDatabase base;
    DatabaseReference reference;
    TextView sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        usernamefield = findViewById(R.id.UserName);
        passwordfield = findViewById(R.id.Password);
        login = findViewById(R.id.idBtnRegister);
        base = FirebaseDatabase.getInstance();
        reference = base.getReference();
        sign = findViewById(R.id.Signup);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernamefield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                if(username.equals(null)){
                    return;
                }
                if(password.equals(null)){
                    return;
                }
                DatabaseReference childref = reference.child("UserList");
                DatabaseReference usename = childref.child(username);
                usename.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String pass = snapshot.child("password").getValue(String.class);
                            if(pass.equals(password)){
                                Toast.makeText(LogIn.this,"Success! You have been logged in.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogIn.this,DepartmentsActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(LogIn.this,"Wrong password",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        else{
                            Toast.makeText(LogIn.this,"No account with this username",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
