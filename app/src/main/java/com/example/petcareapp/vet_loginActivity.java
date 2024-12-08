package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vet_loginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar; // Optional: to show loading state
    private TextView signUpTextView; // TextView for navigating to the Sign Up screen

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_login);

        //  Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // User is signed in, navigate to DashboardActivity
            Intent intent = new Intent(vet_loginActivity.this, vet_dashActivity.class);
            startActivity(intent);
            finish(); // Close this activity to prevent returning back
            return;
        }

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize views
        emailEditText = findViewById(R.id.TextEmailAddress);
        passwordEditText = findViewById(R.id.eTextPassword);
        loginButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar);
        signUpTextView = findViewById(R.id.textView10);

        // Initially hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Click event for the login button
        loginButton.setOnClickListener(v -> loginClinic());

        // Set up OnClickListener for the Sign Up TextView
        signUpTextView.setOnClickListener(v -> {
            // Intent to start the Sign Up Activity
            Intent intent = new Intent(vet_loginActivity.this, vet_signupActivity.class);
            startActivity(intent);
        });
    }

    private void loginClinic() {
        // Get input from EditTexts
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Input validation
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

        // Show loading state (optional)
        progressBar.setVisibility(View.VISIBLE);

        // Sign in the user using Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        // Fetch clinic details from Firebase Realtime Database
                        databaseReference.orderByChild("ownerId").equalTo(userId)
                                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                Clinic clinic = snapshot.getValue(Clinic.class);

                                                if (clinic != null) {
                                                    Toast.makeText(vet_loginActivity.this,
                                                            "Welcome, " + clinic.getClinicName(),
                                                            Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(vet_loginActivity.this, vet_dashActivity.class);
                                                    intent.putExtra("CLINIC_NAME", clinic.getClinicName());
                                                    intent.putExtra("CLINIC_EMAIL", clinic.getEmail());
                                                    intent.putExtra("CLINIC_ID", clinic.getClinicId());
                                                    startActivity(intent);
                                                    finish();
                                                    break;
                                                }
                                            }
                                        } else {
                                            Toast.makeText(vet_loginActivity.this,
                                                    "No clinic information found for this user",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(vet_loginActivity.this,
                                                "Failed to load clinic information: " + databaseError.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        Toast.makeText(vet_loginActivity.this,
                                "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
