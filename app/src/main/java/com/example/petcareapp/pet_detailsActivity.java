package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class pet_detailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Set up leftArrow to open pet_profileActivity
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(v -> {
            Intent intent = new Intent(pet_detailsActivity.this, pet_profileActivity.class);
            startActivity(intent);
        });

        // Fetch and display pet information
        fetchPetDetails();
    }

    private void fetchPetDetails() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid(); // Get the authenticated user's ID

            userRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the User object
                        User user = dataSnapshot.getValue(User.class);

                        if (user != null && user.getPetInformation() != null) {
                            // Fetch the first pet's details
                            String petId = user.getPetInformation().keySet().iterator().next(); // Get the first pet ID
                            Map<String, Object> petDetails = user.getPetDetails(petId);

                            // Get pet details from the map
                            String petName = (String) petDetails.get("petName");
                            String petGender = (String) petDetails.get("gender");
                            String petBreed = (String) petDetails.get("breed");
                            String petBirth = (String) petDetails.get("dob");

                            // Display the pet information in the TextViews
                            TextView petNameTextView = findViewById(R.id.textPet_Name);
                            TextView petGenderTextView = findViewById(R.id.textPet_Gender);
                            TextView petBreedTextView = findViewById(R.id.textPet_Breed);
                            TextView petBirthTextView = findViewById(R.id.textPet_Birth);

                            petNameTextView.setText(petName);
                            petGenderTextView.setText(petGender);
                            petBreedTextView.setText(petBreed);
                            petBirthTextView.setText(petBirth);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle errors
                }
            });
        }
    }
}
