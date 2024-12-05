package com.example.petcareapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_petActivity extends AppCompatActivity {

    private EditText editText4, editText5, editText6;
    private Button saveButton;
    private String petKey; // A key to identify the pet in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_pet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the EditTexts
        editText4 = findViewById(R.id.editText4); // For pet name
        editText5 = findViewById(R.id.editText5); // For pet birthdate
        editText6 = findViewById(R.id.editText6); // For pet breed
        saveButton = findViewById(R.id.saveButton); // Save button

        // Fetch pet information from Firebase Realtime Database
        fetchPetInformation();

        // Set listener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePetInformation();
            }
        });
    }

    private void fetchPetInformation() {
        // Get Firebase references
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Access the pet information node
        databaseReference.child(userId).child("petInformation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming a single pet's information for simplicity, you could adjust this logic if multiple pets exist
                    for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                        // Retrieve pet information
                        String petName = petSnapshot.child("petName").getValue(String.class);
                        String petBirth = petSnapshot.child("dob").getValue(String.class); // "dob" stands for date of birth
                        String petBreed = petSnapshot.child("breed").getValue(String.class);

                        // Save pet key (ID) to update the right pet
                        petKey = petSnapshot.getKey(); // Unique key for the pet

                        // Set data to EditTexts
                        editText4.setText(petName);
                        editText5.setText(petBirth);
                        editText6.setText(petBreed);

                        break; // Exit loop after fetching the first pet (if multiple pets exist)
                    }
                } else {
                    Toast.makeText(edit_petActivity.this, "No pet information found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database errors
                Toast.makeText(edit_petActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePetInformation() {
        // Get the updated pet information from EditTexts
        String updatedPetName = editText4.getText().toString().trim();
        String updatedPetBirth = editText5.getText().toString().trim();
        String updatedPetBreed = editText6.getText().toString().trim();

        // Check if any field is empty
        if (updatedPetName.isEmpty() || updatedPetBirth.isEmpty() || updatedPetBreed.isEmpty()) {
            Toast.makeText(edit_petActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get Firebase references
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Check if petKey is not null (ensuring that we are updating an existing pet)
        if (petKey != null) {
            // Update the specific pet information in Firebase
            DatabaseReference petReference = databaseReference.child(userId).child("petInformation").child(petKey);

            petReference.child("petName").setValue(updatedPetName);
            petReference.child("dob").setValue(updatedPetBirth);
            petReference.child("breed").setValue(updatedPetBreed);

            // Notify the user and finish the activity
            Toast.makeText(edit_petActivity.this, "Pet details updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(edit_petActivity.this, "Error: Pet not found", Toast.LENGTH_SHORT).show();
        }
    }
}
