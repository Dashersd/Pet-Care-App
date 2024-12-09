package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vet_loginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUpTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ImageView hidePassImageView;
    private boolean isPasswordVisible = false; // To track password visibility

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vet_login);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        emailEditText = findViewById(R.id.TextEmailAddress);
        passwordEditText = findViewById(R.id.eTextPassword);
        loginButton = findViewById(R.id.button4);
        progressBar = findViewById(R.id.progressBar);
        signUpTextView = findViewById(R.id.textView10);
        hidePassImageView = findViewById(R.id.imageView4); // Make sure ID is correct in your layout

        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(v -> loginClinic());

        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(vet_loginActivity.this, vet_signupActivity.class);
            startActivity(intent);
        });

        hidePassImageView.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void loginClinic() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        fetchClinicInfo();
                    } else {
                        Toast.makeText(vet_loginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void fetchClinicInfo() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference.orderByChild("ownerId").equalTo(userId)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            navigateToDashboard(dataSnapshot);
                        } else {
                            displayNoClinicInfoFound();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        displayDatabaseError(databaseError);
                    }
                });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            hidePassImageView.setImageResource(R.drawable.ic_hidepass);
        } else {
            passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            hidePassImageView.setImageResource(R.drawable.purple_showpass);
        }
        isPasswordVisible = !isPasswordVisible;
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void navigateToDashboard(DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Clinic clinic = snapshot.getValue(Clinic.class);
            if (clinic != null) {
                Toast.makeText(vet_loginActivity.this, "Welcome, " + clinic.getClinicName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(vet_loginActivity.this, vet_dashActivity.class);
                intent.putExtra("CLINIC_NAME", clinic.getClinicName());
                intent.putExtra("CLINIC_EMAIL", clinic.getEmail());
                intent.putExtra("CLINIC_ID", clinic.getClinicId());
                startActivity(intent);
                finish();
                break;
            }
        }
    }

    private void displayNoClinicInfoFound() {
        Toast.makeText(vet_loginActivity.this, "No clinic information found for this user", Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }

    private void displayDatabaseError(DatabaseError databaseError) {
        Toast.makeText(vet_loginActivity.this, "Failed to load clinic information: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }
}
