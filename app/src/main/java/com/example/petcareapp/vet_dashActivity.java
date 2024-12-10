package com.example.petcareapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vet_dashActivity extends AppCompatActivity {

    private static final String TAG = "VetDashActivity";

    // Firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference clinicsReference;

    // TextViews from the XML
    private TextView textUserName, textPet, textService, textReason, textDate;

    // Dynamic clinic name for this vet
    private String clinicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vet_dash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Authentication and Database references
        mAuth = FirebaseAuth.getInstance();
        String currentVetId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize TextViews from XML
        textUserName = findViewById(R.id.textUser_Name);
        textPet = findViewById(R.id.textPet);
        textService = findViewById(R.id.textService);
        textReason = findViewById(R.id.textReason);
        textDate = findViewById(R.id.textDate);

        // Get the clinic name dynamically for the current vet
        fetchClinicName(currentVetId);

        // Initialize and set OnClickListener for the profile view
        View profileView = findViewById(R.id.view3);
        profileView.setOnClickListener(v -> {
            Intent intent = new Intent(vet_dashActivity.this, vet_profileActivity.class);
            startActivity(intent);
        });

        // Initialize button24 and set OnClickListener to show the dialog
        Button button24 = findViewById(R.id.button24);
        button24.setOnClickListener(v -> showConfirmationDialog());
    }

    private void fetchClinicName(String vetUserId) {
        databaseReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String ownerId = snapshot.child("ownerId").getValue(String.class);
                    if (ownerId != null && ownerId.equals(vetUserId)) {
                        clinicName = snapshot.child("clinicName").getValue(String.class);
                        if (clinicName != null && !clinicName.isEmpty()) {
                            Log.d(TAG, "Clinic Name Found: " + clinicName);
                            fetchClinicAppointments(clinicName);
                            break;
                        }
                    }
                }
            } else {
                Toast.makeText(this, "No clinics found for this vet.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to fetch clinic name: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void fetchClinicAppointments(String clinicName) {
        // Clean clinic name for Firebase path
        String clinicPath = clinicName.replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();

        clinicsReference = FirebaseDatabase.getInstance().getReference("Clinics")
                .child(clinicPath)
                .child("appointments");

        clinicsReference.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get appointment details directly from the appointment data
                    String userName = snapshot.child("userName").getValue(String.class);
                    String petName = snapshot.child("pet").getValue(String.class);
                    String serviceType = snapshot.child("service").getValue(String.class);
                    String reason = snapshot.child("reason").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);

                    // Update the TextViews with the appointment details
                    textUserName.setText("User: " + (userName != null ? userName : "Unknown User"));
                    textPet.setText("Pet: " + (petName != null ? petName : "N/A"));
                    textService.setText("Service: " + (serviceType != null ? serviceType : "N/A"));
                    textReason.setText("Reason: " + (reason != null ? reason : "N/A"));
                    textDate.setText("Date: " + (date != null ? date : "N/A"));

                    break; // Display only the first appointment
                }
            } else {
                Toast.makeText(this, "No appointments available.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to fetch appointments: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }

    private void showConfirmationDialog() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(vet_dashActivity.this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to proceed?");

        // Set the "Confirm" button
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Open VetLoginActivity
                Intent intent = new Intent(vet_dashActivity.this, vet_loginActivity.class);
                startActivity(intent);
            }
        });

        // Set the "No" button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
