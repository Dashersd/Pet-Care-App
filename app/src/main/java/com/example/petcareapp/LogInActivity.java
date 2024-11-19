package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private ImageView hidePassImageView;
    private boolean isPasswordVisible = false;  // Track password visibility
    private FirebaseAuth mAuth;  // Firebase Auth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        // Apply window insets for padding (e.g., for edge-to-edge layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize the EditText and ImageView
        emailEditText = findViewById(R.id.TextEmailAddress);  // Assuming you have an EditText for email input
        passwordEditText = findViewById(R.id.eTextPassword);
        hidePassImageView = findViewById(R.id.imageView4);

        // Set an OnClickListener for the login button
        Button loginButton = findViewById(R.id.button4);
        loginButton.setOnClickListener(v -> {
            // Get the input email and password
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LogInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Sign in the user with Firebase Authentication
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Show a success toast message
                            Toast.makeText(LogInActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                            // If login is successful, navigate to DashboardActivity
                            Intent intent = new Intent(LogInActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // If login fails, show error message
                            Toast.makeText(LogInActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set an OnClickListener for the ImageView to toggle password visibility
        hidePassImageView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Hide password
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.ic_hidepass); // Change icon to "hide" icon
            } else {
                // Show password
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.purple_showpass); // Change icon to "show" icon
            }
            isPasswordVisible = !isPasswordVisible; // Toggle the flag

            // Move cursor to the end of the text
            passwordEditText.setSelection(passwordEditText.getText().length());
        });
    }
}
