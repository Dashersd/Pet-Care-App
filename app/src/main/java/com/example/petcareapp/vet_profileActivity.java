package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vet_profileActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView clinicNameTextView, clinicEmailTextView;

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vet_profile);

        // Set edge-to-edge layout adjustments
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize views
        clinicNameTextView = findViewById(R.id.clinicNameTextView);
        clinicEmailTextView = findViewById(R.id.clinicEmailTextView);

        // Find the ImageView by its ID
        ImageView leftArrow = findViewById(R.id.leftArrow);

        // Set OnClickListener to the ImageView (Back Button)
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to vet_dashActivity
                Intent intent = new Intent(vet_profileActivity.this, vet_dashActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });

        // Fetch clinic details from Firebase and display them in the TextViews
        fetchClinicDetails();
    }

    private void fetchClinicDetails() {
        // Get the current user ID from Firebase Authentication
        String userId = mAuth.getCurrentUser().getUid();

        // Query the database to get the clinic with the matching ownerId
        databaseReference.orderByChild("ownerId").equalTo(userId)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Loop through all clinics that match the ownerId
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Clinic clinic = snapshot.getValue(Clinic.class);

                                if (clinic != null) {
                                    // Display clinic information on the screen
                                    clinicNameTextView.setText(clinic.getClinicName());
                                    clinicEmailTextView.setText(clinic.getEmail());
                                    break; // Exit loop after retrieving the first match
                                }
                            }
                        } else {
                            Toast.makeText(vet_profileActivity.this,
                                    "No clinic information found for this user",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(vet_profileActivity.this,
                                "Failed to load clinic information",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
