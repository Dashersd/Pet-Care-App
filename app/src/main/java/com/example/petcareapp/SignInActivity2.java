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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in2);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        petSpinner = findViewById(R.id.pet_spinner);
        petNameEditText = findViewById(R.id.PetNameText);
        dateOfBirthEditText = findViewById(R.id.editTextDate);
        breedEditText = findViewById(R.id.editTextText4);
        maleCheckBox = findViewById(R.id.MalecheckBox);
        femaleCheckBox = findViewById(R.id.FemalecheckBox2);

        // Setup window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Spinner setup for pet options
        String[] petOptions = {"Select an option...", "Dog", "Cat"};
        ArrayAdapter<String> petAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, petOptions) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;  // Disable the first item (hint)
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);  // Hint item color
                } else {
                    tv.setTextColor(Color.BLACK);  // Regular item color
                }
                return view;
            }
        };
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(petAdapter);

        // Handle the button click to save data and go to DashboardActivity
        Button button = findViewById(R.id.button5);
        button.setOnClickListener(v -> {
            // Get the data from user input
            String petName = petNameEditText.getText().toString().trim();
            String dateOfBirth = dateOfBirthEditText.getText().toString().trim();
            String breed = breedEditText.getText().toString().trim();
            String petType = petSpinner.getSelectedItem().toString();
            String gender = "";

            // Determine gender based on checkbox selection
            if (maleCheckBox.isChecked()) {
                gender = "Male";
            } else if (femaleCheckBox.isChecked()) {
                gender = "Female";
            }

            // Validation check
            if (petName.isEmpty() || dateOfBirth.isEmpty() || breed.isEmpty() || petType.equals("Select an option...") || gender.isEmpty()) {
                Toast.makeText(SignInActivity2.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve current user ID from Firebase Authentication
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Create a unique pet ID (you can use a unique ID generator or Firebase push ID)
                String petId = mDatabase.child("users").child(userId).child("petInformation").push().getKey();

                // Create User object and add pet information
                User user = new User(userId, "User Name", "user@example.com"); // Replace with actual user data
                user.addPet(petId, petName, dateOfBirth, gender, breed, petType);

                // Store user data in Firebase
                mDatabase.child("users").child(userId).setValue(user);

                // Navigate to DashboardActivity
                Intent intent = new Intent(SignInActivity2.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Handle TextView click to navigate to DashboardActivity (Skip button)
        TextView skipTextView = findViewById(R.id.textView17);
        skipTextView.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity2.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}
