package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextInputEditText emailUser, passwordUser;
    CheckBox checkBoxes;
    Button btLogin;
    TextView forgotPass, signUp;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inisialisasi Views
        emailUser = findViewById(R.id.email);
        passwordUser = findViewById(R.id.password);
        checkBoxes = findViewById(R.id.checkboxes);
        btLogin = findViewById(R.id.btnLogin);
        forgotPass = findViewById(R.id.forgotPassword);
        signUp = findViewById(R.id.signUp);

        // Login functionality
        btLogin.setOnClickListener(view -> {
            String email = emailUser.getText().toString().trim();
            String password = passwordUser.getText().toString().trim();

            // Cek apakah email dan password kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt to sign in the user using Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Login berhasil, pindah ke HomeActivity
                            Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish(); // Menutup MainActivity agar pengguna tidak bisa kembali ke halaman login
                        } else {
                            // Login gagal, tampilkan pesan kesalahan
                            Toast.makeText(MainActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // SignUp functionality
        signUp.setOnClickListener(view -> {
            // Intent untuk berpindah ke MainActivity2 (halaman pendaftaran)
            Intent intent = new Intent(MainActivity.this, MainActivity2.class); // MainActivity2 adalah halaman sign-up
            startActivity(intent);
        });
    }
}
