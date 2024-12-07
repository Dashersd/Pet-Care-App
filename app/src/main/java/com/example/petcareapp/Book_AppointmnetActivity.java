package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Book_AppointmnetActivity extends AppCompatActivity {

    private Spinner petSpinner;
    private Spinner serviceSpinner;
    private EditText reasonEditText;
    private String currentUserId;
    private DatabaseReference databaseReference;
    private String clinicName = "Happy Paws Veterinary Clinic"; // For example, dynamic clinic name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointmnet);

        // Initialize Firebase Authentication and Database Reference
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid(); // Retrieve the logged-in user's ID
        } else {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show();
            finish(); // Close activity if no user is logged in
            return;
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        // Initialize UI components
        petSpinner = findViewById(R.id.pet_spinner);
        serviceSpinner = findViewById(R.id.services_spinner);
        reasonEditText = findViewById(R.id.reason_text);

        // Set up Edge-to-Edge window insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the back arrow click listener
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            // Open the Clinic_ProfileActivity
            Intent intent = new Intent(Book_AppointmnetActivity.this, Clinic_ProfileActivity.class);
            startActivity(intent);
        });

        // Set up the Spinner for selecting pets
        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Dog", "Cat", "Other"}); // Replace with dynamic values if needed
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(petAdapter);

        // Set up the Spinner for selecting services
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Vaccination", "Grooming", "Checkup", "Surgery"});
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);

        // Set up the next_button click listener
        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            // Retrieve user inputs
            String selectedPet = petSpinner.getSelectedItem().toString();
            String selectedService = serviceSpinner.getSelectedItem().toString();
            String reason = reasonEditText.getText().toString().trim();

            // Validate inputs
            if (selectedPet.isEmpty() || selectedService.isEmpty() || reason.isEmpty()) {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save data to Firebase
            saveAppointmentDetailsToFirebase(selectedPet, selectedService, reason);

            // Navigate to Book_Appointment2Activity
            Intent intent = new Intent(Book_AppointmnetActivity.this, Book_Appointment2Activity.class);
            intent.putExtra("clinic_name", clinicName); // Dynamic clinic name
            startActivity(intent);
        });
    }

    private void saveAppointmentDetailsToFirebase(String pet, String service, String reason) {
        // Create a map for storing appointment details
        Map<String, Object> appointmentDetails = new HashMap<>();
        appointmentDetails.put("selectedPet", pet);
        appointmentDetails.put("selectedService", service);
        appointmentDetails.put("reason", reason);

        // Save to Firebase under the user's ID
        databaseReference.child("appointmentDetails").setValue(appointmentDetails)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Details saved successfully.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
