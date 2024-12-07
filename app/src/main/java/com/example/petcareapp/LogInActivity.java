package com.example.petcareapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private ImageView hidePassImageView;
    private boolean isPasswordVisible = false; // Track password visibility
    private FirebaseAuth mAuth; // Firebase Auth instance
    private ProgressBar progressBar; // Progress bar created programmatically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create root layout programmatically (if not using setContentView)
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setPadding(32, 32, 32, 32);
        rootLayout.setGravity(Gravity.CENTER);
        rootLayout.setBackgroundColor(Color.WHITE);

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = new EditText(this);
        emailEditText.setHint("Email");
        emailEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        passwordEditText = new EditText(this);
        passwordEditText.setHint("Password");
        passwordEditText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        hidePassImageView = new ImageView(this);
        hidePassImageView.setImageResource(R.drawable.ic_hidepass); // Replace with your hidepass image
        hidePassImageView.setLayoutParams(new LinearLayout.LayoutParams(100, 100));

        Button loginButton = new Button(this);
        loginButton.setText("Log In");
        loginButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create ProgressBar programmatically
        progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        progressBar.setVisibility(View.GONE); // Hide it initially

        // Add views to root layout
        rootLayout.addView(emailEditText);
        rootLayout.addView(passwordEditText);
        rootLayout.addView(hidePassImageView);
        rootLayout.addView(loginButton);
        rootLayout.addView(progressBar); // Add the programmatic progress bar

        // Set the root layout as the activity's content view
        setContentView(rootLayout);

        // Set OnClickListener for login button
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Enter a valid email");
                emailEditText.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Password is required");
                passwordEditText.requestFocus();
                return;
            }

            if (password.length() < 6) {
                passwordEditText.setError("Password must be at least 6 characters");
                passwordEditText.requestFocus();
                return;
            }

            // Show ProgressBar and disable the login button
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);

            // Authenticate with Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        loginButton.setEnabled(true); // Re-enable login button
                        passwordEditText.setText(""); // Clear password

                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LogInActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish(); // Close this activity
                        } else {
                            Toast.makeText(LogInActivity.this,
                                    "Authentication failed. Please check your credentials.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Set OnClickListener for password visibility toggle
        hidePassImageView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.ic_hidepass); // Replace with your "hide" icon
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                hidePassImageView.setImageResource(R.drawable.purple_showpass); // Replace with your "show" icon
            }
            isPasswordVisible = !isPasswordVisible;
            passwordEditText.setSelection(passwordEditText.getText().length()); // Maintain cursor position
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        moveTaskToBack(true); // Move app to background instead of closing
    }
}
