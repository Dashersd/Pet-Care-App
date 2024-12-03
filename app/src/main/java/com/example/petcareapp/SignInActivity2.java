package com.example.petcareapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity2 extends AppCompatActivity {

    Spinner petSpinner;
    EditText petNameEditText, dateOfBirthEditText, breedEditText;
    CheckBox maleCheckBox, femaleCheckBox;
    private DatabaseReference mDatabase;
    private String name, email, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in2);

        // Retrieve data from Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        // Ensure Firebase Authentication is initialized
        userId = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        petSpinner = findViewById(R.id.pet_spinner);
        petNameEditText = findViewById(R.id.PetNameText);
        dateOfBirthEditText = findViewById(R.id.editTextDate);
        breedEditText = findViewById(R.id.editTextText4);
        maleCheckBox = findViewById(R.id.MalecheckBox);
        femaleCheckBox = findViewById(R.id.FemalecheckBox2);

        // Spinner setup for pet options
        String[] petOptions = {"Select an option...", "Dog", "Cat"};
        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, petOptions);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(petAdapter);

        // Save Pet Information
        Button button = findViewById(R.id.button5);
        button.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(SignInActivity2.this, "User not authenticated!", Toast.LENGTH_SHORT).show();
                return;
            }

            String petName = petNameEditText.getText().toString().trim();
            String dateOfBirth = dateOfBirthEditText.getText().toString().trim();
            String breed = breedEditText.getText().toString().trim();
            String petType = petSpinner.getSelectedItem().toString();
            String gender = maleCheckBox.isChecked() ? "Male" : femaleCheckBox.isChecked() ? "Female" : "";

            if (petName.isEmpty() || dateOfBirth.isEmpty() || breed.isEmpty() || petType.equals("Select an option...") || gender.isEmpty()) {
                Toast.makeText(SignInActivity2.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                String petId = mDatabase.child("users").child(userId).child("petInformation").push().getKey();

                DatabaseReference petRef = mDatabase.child("users").child(userId).child("petInformation").child(petId);
                petRef.child("petName").setValue(petName);
                petRef.child("dob").setValue(dateOfBirth);
                petRef.child("gender").setValue(gender);
                petRef.child("breed").setValue(breed);
                petRef.child("petType").setValue(petType);

                Intent intentToDashboard = new Intent(SignInActivity2.this, DashboardActivity.class);
                startActivity(intentToDashboard);
                finish();
            }
        });

        // Skip button
        TextView skipTextView = findViewById(R.id.textView17);
        skipTextView.setOnClickListener(v -> {
            if (userId != null) {
                // Ensure basic user data is saved before skipping
                mDatabase.child("users").child(userId).child("name").setValue(name);
                mDatabase.child("users").child(userId).child("email").setValue(email);
            }

            Intent intentToDashboard = new Intent(SignInActivity2.this, DashboardActivity.class);
            startActivity(intentToDashboard);
            finish();
        });
    }
}
