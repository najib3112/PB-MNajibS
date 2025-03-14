package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Models.UserDetails;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser ;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    EditText usernameEditText, emailEditText, nimEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Inisialisasi EditText
        usernameEditText = findViewById(R.id.editTextText);
        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        nimEditText = findViewById(R.id.editTextText2);

        // Ambil UID pengguna yang sedang login
        FirebaseUser  currentUser  = FirebaseAuth.getInstance().getCurrentUser ();
        if (currentUser  != null) {
            String uid = currentUser .getUid();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        UserDetails userDetails = dataSnapshot.getValue(UserDetails.class);
                        if (userDetails != null) {
                            usernameEditText.setText(userDetails.getUsername());
                            emailEditText.setText(userDetails.getEmail());
                            nimEditText.setText(userDetails.getNim());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                    Log.e("ProfileActivity", "Database error: " + databaseError.getMessage());
                }
            });
        }
    }
}