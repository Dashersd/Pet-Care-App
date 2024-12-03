package com.example.petcareapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private ImageView hidePassImageView;
    private boolean isPasswordVisible = false; // Track password visibility
    private FirebaseAuth mAuth; // Firebase Auth instance
    private ProgressDialog progressDialog; // For showing a loading indicator

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.TextEmailAddress);
        passwordEditText = findViewById(R.id.eTextPassword);
        hidePassImageView = findViewById(R.id.imageView4);
        Button loginButton = findViewById(R.id.button4);

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        // Set OnClickListener for login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate inputs
            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            // Show progress dialog
            progressDialog.show();

            // Authenticate with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        // Dismiss progress dialog
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Navigate to DashboardActivity
                            Intent intent = new Intent(LogInActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            // Show error message
                            Toast.makeText(LogInActivity.this,
                                    "Authentication failed. Please check your credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set OnClickListener for password visibility toggle
        hidePassImageView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.ic_hidepass); // Replace with your "hide" icon
            } else {
                // Show password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.purple_showpass); // Replace with your "show" icon
            }
            isPasswordVisible = !isPasswordVisible; // Toggle the flag
            passwordEditText.setSelection(passwordEditText.getText().length()); // Maintain cursor position
        });
    }
}
