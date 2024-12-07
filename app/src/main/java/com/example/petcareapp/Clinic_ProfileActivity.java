package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Clinic_ProfileActivity extends AppCompatActivity {

    private String clinicName = "Happy Paws Veterinary Clinic"; // Replace with dynamic value if needed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic_profile);

        // Set up window insets handling for Edge-to-Edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back Button to return to Clinic_SelectionActivity
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Clinic_ProfileActivity.this, Clinic_SelectionActivity.class);
            startActivity(intent);
        });

        // Book Appointment Button
        Button bookAppointmentButton = findViewById(R.id.button20);
        bookAppointmentButton.setOnClickListener(v -> {
            // If clinicName is dynamic, retrieve it here
            // Example: Fetch from a TextView or a Database
            // clinicName = getIntent().getStringExtra("clinic_name");

            // Navigate to Book_AppointmnetActivity and pass clinic name
            Intent intent = new Intent(Clinic_ProfileActivity.this, Book_AppointmnetActivity.class);
            intent.putExtra("clinic_name", clinicName);
            startActivity(intent);
        });
    }
}
