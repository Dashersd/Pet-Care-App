package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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

    // Declare EditText and ImageView
    private EditText nameEditText, emailEditText, passwordEditText;
    private ImageView togglePasswordView;
    private boolean isPasswordVisible = false; // Flag to check if the password is visible

    // Declare Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Initialize the views
        nameEditText = findViewById(R.id.editTextName);
        emailEditText = findViewById(R.id.EmailAddressText);
        passwordEditText = findViewById(R.id.TextPassword);
        togglePasswordView = findViewById(R.id.imageView12);

        // Set up password visibility toggle
        togglePasswordView.setOnClickListener(v -> {
            if (isPasswordVisible) {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                togglePasswordView.setImageResource(R.drawable.orange_hidepass); // Update icon to "hide"
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                togglePasswordView.setImageResource(R.drawable.orange_showpass); // Update icon to "show"
            }
            passwordEditText.setSelection(passwordEditText.length()); // Keep cursor at the end
            isPasswordVisible = !isPasswordVisible;
        });

        // Initialize sign-in button
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(v -> {
            // Get input values
            String name = nameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Validate input fields
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new user with Firebase Authentication
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Successfully signed up, store additional data in Realtime Database
                            String userId = mAuth.getCurrentUser().getUid();

                            // Create a map to store user data
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);

                            // Initialize pet information as a nested map (if needed)
                            Map<String, Object> petDetails = new HashMap<>();
                            petDetails.put("petName", "");  // Initialize as empty, or fill with actual values if available
                            petDetails.put("dob", "");
                            petDetails.put("gender", "");
                            petDetails.put("breed", "");
                            petDetails.put("petType", "");

                            // Adding a pet entry with a unique pet ID
                            userData.put("petInformation", new HashMap<String, Object>() {{
                                put("initialPetId", petDetails);
                            }});

                            // Store user data in the database
                            DatabaseReference usersRef = mDatabase.getReference("users").child(userId);
                            usersRef.setValue(userData).addOnCompleteListener(dbTask -> {
                                if (dbTask.isSuccessful()) {
                                    Toast.makeText(SignInActivity.this, "User profile created.", Toast.LENGTH_SHORT).show();
                                    // Navigate to the next activity
                                    Intent intent = new Intent(SignInActivity.this, SignInActivity2.class);
                                    startActivity(intent);
                                    finish(); // Finish this activity so it doesn't stay in the back stack
                                } else {
                                    Toast.makeText(SignInActivity.this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
