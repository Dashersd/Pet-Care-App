package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

public class DashboardActivity extends AppCompatActivity {

    private TextView userNameTextView, petNameTextView, petBreedTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Apply window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth and DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI elements for text display
        userNameTextView = findViewById(R.id.textView19);
        petNameTextView = findViewById(R.id.textView20);
        petBreedTextView = findViewById(R.id.textView22);

        // Fetch the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Retrieve user data from Firebase
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Fetch user name from the main user node (fallback)
                    String userName = dataSnapshot.child("name").getValue(String.class);

                    // Check if petInformation exists
                    if (dataSnapshot.hasChild("petInformation")) {
                        DataSnapshot petSnapshot = dataSnapshot.child("petInformation").getChildren().iterator().next();

                        // Attempt to fetch userName from petInformation
                        String petUserName = petSnapshot.child("userName").getValue(String.class);
                        if (petUserName != null) {
                            userName = petUserName; // Use the userName from petInformation if available
                        }

                        // Fetch pet details
                        String petName = petSnapshot.child("petName").getValue(String.class);
                        String petBreed = petSnapshot.child("breed").getValue(String.class);

                        // Update UI
                        if (userName != null) userNameTextView.setText("Hello, " + userName);
                        if (petName != null) petNameTextView.setText(petName);
                        if (petBreed != null) petBreedTextView.setText(petBreed);
                    } else {
                        // If no pet information exists, fallback to main userName
                        if (userName != null) userNameTextView.setText("Hello, " + userName);
                        Toast.makeText(DashboardActivity.this, "No pet information found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DashboardActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DashboardActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup button click to navigate to AppointmentActivity
        Button button = findViewById(R.id.button7);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AppoinmentActivity.class);
                startActivity(intent);
            }
        });

        // Setup image view click to navigate to AppointmentActivity
        ImageView imageView = findViewById(R.id.imageView31);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, AppoinmentActivity.class);
                startActivity(intent);
            }
        });

        // Setup button8 click to navigate to PetProfileActivity
        Button button8 = findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, pet_profileActivity.class);
                startActivity(intent);
            }
        });
    }
}
