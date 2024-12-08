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

public class Clinic_SelectionActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView clinicNameTextView1;
    private Button viewProfileButton, bookAppointmentButton;

    // Firebase instances
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic_selection);

        // Handle system window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize views
        clinicNameTextView1 = findViewById(R.id.clinic_name_1);
        viewProfileButton = findViewById(R.id.view_profile_button_1);
        bookAppointmentButton = findViewById(R.id.book_appointment_button_1);

        // Fetch all clinics and display the clinic name
        fetchClinicDetails();

        // Back button click event
        ImageView imageView = findViewById(R.id.imageView36);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the AppointmentActivity
                Intent intent = new Intent(Clinic_SelectionActivity.this, AppoinmentActivity.class);
                startActivity(intent);
            }
        });

        // View Profile button click event
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Clinic_SelectionActivity.this, Clinic_ProfileActivity.class);
                String clinicName = clinicNameTextView1.getText().toString().trim();
                intent.putExtra("clinic_name", clinicName);
                startActivity(intent);
            }
        });

        // Book Appointment button click event
        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String clinicName = clinicNameTextView1.getText().toString().trim();
                Intent intent = new Intent(Clinic_SelectionActivity.this, Book_AppointmnetActivity.class);
                intent.putExtra("clinic_name", clinicName);
                startActivity(intent);
            }
        });
    }

    /**
     * Fetch all clinic details from Firebase and display them in the TextView
     */
    private void fetchClinicDetails() {
        // Query the database to fetch all clinics
        databaseReference.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Retrieve the clinic information
                        Clinic clinic = snapshot.getValue(Clinic.class);

                        if (clinic != null) {
                            // Display the first clinic name in the TextView
                            clinicNameTextView1.setText(clinic.getClinicName());
                            break; // Exit loop after retrieving the first match
                        }
                    }
                } else {
                    Toast.makeText(Clinic_SelectionActivity.this,
                            "No clinics available.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Clinic_SelectionActivity.this,
                        "Failed to fetch clinic details.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
