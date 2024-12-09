package com.example.petcareapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Pet_OwnerLogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private ImageView hidePassImageView;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_owner_log_in);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Link views from XML
        emailEditText = findViewById(R.id.TextEmailAddress);
        passwordEditText = findViewById(R.id.eTextPassword);
        loginButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar);
        hidePassImageView = findViewById(R.id.imageView4); // Updated to use correct ImageView ID

        // Set initial visibility of ProgressBar to GONE
        progressBar.setVisibility(View.GONE);

        // Login button click listener
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (!validateInputs(email, password)) return;

            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Pet_OwnerLogInActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Pet_OwnerLogInActivity.this,
                                    "Authentication failed. Please check your email and password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        Toast.makeText(Pet_OwnerLogInActivity.this,
                                "Error: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
            TextView textView10 = findViewById(R.id.textView10);

            textView10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Pet_OwnerLogInActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
            });
        });

        // Set OnClickListener for password visibility toggle
        hidePassImageView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.ic_hidepass); // Set to "hide" icon
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.purple_showpass); // Set to "show" icon
            }
            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.getText().length()); // Maintain cursor position
        });
    }

    /**
     * Validates user inputs for email and password.
     * @param email The email entered by the user.
     * @param password The password entered by the user.
     * @return True if inputs are valid, false otherwise.
     */
    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Enter a valid email");
            emailEditText.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Call super first to ensure proper lifecycle handling
        moveTaskToBack(true); // Move the app to the background instead of closing it
    }
}
