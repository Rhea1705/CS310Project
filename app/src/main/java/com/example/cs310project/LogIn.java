package com.example.cs310project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    EditText emailfield,passwordfield;
    Button login;
    FirebaseDatabase base;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    TextView sign;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        emailfield = findViewById(R.id.ID);
        passwordfield = findViewById(R.id.Password);
        login = findViewById(R.id.idBtnRegister);
        base = FirebaseDatabase.getInstance();
        reference = base.getReference();
        sign = findViewById(R.id.Signup);
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailfield.getText().toString().trim();
                String password = passwordfield.getText().toString().trim();
                Log.d("Login", "email is" + email);
                Log.d("Login", "passowrd is" + password);
                if(email.equals(null)){
                    return;
                }
                if(password.equals(null)){
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LogIn.this,"Success! You have been logged in.",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LogIn.this,DepartmentsActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LogIn.this,"Error" + task.getException(),Toast.LENGTH_SHORT).show();
                                    return;
                                }
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
