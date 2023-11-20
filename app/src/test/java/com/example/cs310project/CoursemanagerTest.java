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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CoursemanagerTest {

    @InjectMocks
    private Coursemanager coursemanager;

    @Mock
    private FirebaseAuth mAuth;

    @Mock
    private FirebaseUser firebaseUser;

    @Mock
    private DatabaseReference mockCourseref;

    @Mock
    private DataSnapshot snapshot;

    @Mock
    private ValueEventListener valueEventListener;

    private Course course;
    private List<User> roster;




    @Test
    public void increaserostertest() {

        int num = 0;
        int newnum = Coursemanager.increase_rostersize(num);


        assertEquals(1, newnum );
        assertNotEquals(3,newnum);

    }

    @Test
    public void decreaserostertest() {

        int num = 3;
        int newnum = Coursemanager.decrease_rostersize(num);


        assertEquals(2, newnum );

    }


}
