package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Models.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    Button signUpBtn;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;
    FirebaseAuth mAuth;
    private static final String TAG = "MainActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Inisialisasi view dan Firebase Auth
        signUpBtn = findViewById(R.id.signUpBtn);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSignUp = findViewById(R.id.passwordSingUp);
        nimPengguna = findViewById(R.id.nimPengguna);

        // Inisialisasi FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set onClickListener untuk tombol SignUp
        signUpBtn.setOnClickListener(view -> {
            String username = usernameSignUp.getText().toString().trim();
            String email = emailPengguna.getText().toString().trim();
            String password = passwordSignUp.getText().toString().trim();
            String nim = nimPengguna.getText().toString().trim();

            // Validasi input
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(MainActivity2.this, "Enter Username", Toast.LENGTH_LONG).show();
                usernameSignUp.requestFocus();
            } else if (TextUtils.isEmpty(email)) {
                Toast.makeText(MainActivity2.this, "Enter email", Toast.LENGTH_LONG).show();
                emailPengguna.requestFocus();
            } else if (TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity2.this, "Enter Password", Toast.LENGTH_LONG).show();
                passwordSignUp.requestFocus();
            } else if (TextUtils.isEmpty(nim)) {
                Toast.makeText(MainActivity2.this, "Please Insert your NIM", Toast.LENGTH_LONG).show();
                nimPengguna.requestFocus();
            } else {
                // Registrasi pengguna
                registerUser(username, email, password, nim);
            }
        });
    }

    private void registerUser(String username, String email, String password, String nim) {
        // Menggunakan FirebaseAuth untuk membuat user baru dengan email dan password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity2.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser fUser = mAuth.getCurrentUser();
                String uid = fUser.getUid();

                // Membuat objek UserDetails untuk menyimpan informasi pengguna ke Realtime Database
                UserDetails userDetails = new UserDetails(uid, username, email, password, nim);

                // Menyimpan data pengguna ke Firebase Realtime Database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                reference.child(fUser.getUid()).setValue(userDetails).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        // Kirim email verifikasi jika pendaftaran berhasil
                        fUser.sendEmailVerification();
                        Toast.makeText(MainActivity2.this, "Account created", Toast.LENGTH_LONG).show();

                        // Pindah ke halaman HomeActivity setelah berhasil mendaftar
                        Intent intent = new Intent(MainActivity2.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // Menampilkan pesan error jika penyimpanan data gagal
                        Toast.makeText(MainActivity2.this, "Account registration failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Register: Error");
                    }
                });
            } else {
                // Menampilkan pesan error jika pendaftaran gagal
                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Toast.makeText(MainActivity2.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity2.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
