package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vet_signupActivity extends AppCompatActivity {

    private EditText passwordEditText, clinicNameEditText, emailEditText; // Input fields
    private ImageView togglePasswordView; // ImageView to toggle password visibility
    private Button signupButton; // Button to trigger the registration process
    private boolean isPasswordVisible = false; // To track the visibility state of the password

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_signup);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize views
        passwordEditText = findViewById(R.id.TextPassword);
        clinicNameEditText = findViewById(R.id.editTextName); // Clinic name input
        emailEditText = findViewById(R.id.EmailAddressText); // Email input
        togglePasswordView = findViewById(R.id.imageView12); // Toggle password visibility icon
        signupButton = findViewById(R.id.button3); // Sign up button

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets.consumeSystemWindowInsets();
        });

        // Click event to go to the login screen
        TextView textView = findViewById(R.id.textView16);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(vet_signupActivity.this, vet_loginActivity.class);
                startActivity(intent);
            }
        });

        // Toggle password visibility
        togglePasswordView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                togglePasswordView.setImageResource(R.drawable.orange_hidepass);
            } else {
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                togglePasswordView.setImageResource(R.drawable.orange_showpass);
            }
            passwordEditText.setSelection(passwordEditText.length());
            isPasswordVisible = !isPasswordVisible;
        });

        // Sign up button click event
        signupButton.setOnClickListener(v -> registerClinic());
    }

    private void registerClinic() {
        // Get input from EditTexts
        String clinicName = clinicNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Input validation
        if (clinicName.isEmpty()) {
            clinicNameEditText.setError("Clinic name is required");
            clinicNameEditText.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
            return;
        }

        // Register the user using Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get the unique user ID from Firebase Authentication
                        String ownerId = mAuth.getCurrentUser().getUid();

                        // Generate a unique ID for the clinic in Firebase
                        String clinicId = databaseReference.push().getKey();

                        // Create a Clinic object with the necessary information
                        Clinic clinic = new Clinic(clinicId, clinicName, email, ownerId);

                        // Store the clinic information in the 'Clinics' node in Firebase
                        databaseReference.child(clinicId).setValue(clinic)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(vet_signupActivity.this,
                                                "Clinic Registered Successfully",
                                                Toast.LENGTH_LONG).show();

                                        // Optionally, redirect to the login screen or dashboard
                                        Intent intent = new Intent(vet_signupActivity.this, vet_dashActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(vet_signupActivity.this,
                                                "Failed to save clinic information",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Toast.makeText(vet_signupActivity.this,
                                "Registration Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
