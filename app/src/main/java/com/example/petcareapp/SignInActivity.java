package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText;
    private ImageView togglePasswordView;
    private boolean isPasswordVisible = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.EmailAddressText);
        passwordEditText = findViewById(R.id.TextPassword);
        togglePasswordView = findViewById(R.id.imageView12);

        // Toggle password visibility
        togglePasswordView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
                togglePasswordView.setImageResource(R.drawable.orange_hidepass);
            } else {
                passwordEditText.setTransformationMethod(new SingleLineTransformationMethod());
                togglePasswordView.setImageResource(R.drawable.orange_showpass);
            }
            passwordEditText.setSelection(passwordEditText.length());
            isPasswordVisible = !isPasswordVisible;
        });

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();

                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);

                            DatabaseReference usersRef = mDatabase.getReference("users").child(userId);
                            usersRef.setValue(userData).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Intent intent = new Intent(SignInActivity.this, SignInActivity2.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
