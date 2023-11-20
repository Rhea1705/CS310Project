package com.example.cs310project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Coursemanager {

    public static void addStudentToRoster(List<User> ros, Course currCourse, String whattodo, DatabaseReference courseref){
        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference ref = base.getReference();
        DatabaseReference childref = ref.child("UserList");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String uuid = firebaseUser.getUid();
        DatabaseReference useref = childref.child(uuid);

        useref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    return;
                }
                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String password = snapshot.child("password").getValue(String.class);
                String phone_number = snapshot.child("phone_number").getValue(String.class);
                String usc_id = snapshot.child("usc_id").getValue(String.class);
                String role = snapshot.child("role").getValue(String.class);
                String username = snapshot.child("username").getValue(String.class);
                Log.d("My app",name + email);

                if(whattodo.equals("add")){
                    User user = new User(name,email,password,phone_number,usc_id,role,username,uuid);
                    ros.add(user);
                    courseref.child(currCourse.name).child("roster").child(uuid).setValue(user);
                }
                else if(whattodo.equals("remove")){
                    User remove = null;
                    Log.d("Courseapp","enters remove");
                    for(User use: ros) {

                        if (use.getUid().equals(uuid)) {
                            remove = use;

                        }
                    }
                    if(remove != null){
                        ros.remove(remove);
                        courseref.child(currCourse.name).child("roster").child(uuid).removeValue();
                    }
                }
                currCourse.setRoster(ros);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static int increase_rostersize(int current_size){
        return current_size+1;
    }
    public static int decrease_rostersize(int current_size){
        return current_size-1;
    }
}
