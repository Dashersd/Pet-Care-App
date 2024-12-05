package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Book_AppointmnetActivity extends AppCompatActivity {

    private Spinner petSpinner;
    private Spinner serviceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointmnet);

        // Initialize the spinners
        petSpinner = findViewById(R.id.pet_spinner);
        serviceSpinner = findViewById(R.id.services_spinner);

        // Set up Edge-to-Edge window insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the back arrow click listener
        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(v -> {
            // Open the new activity
            Intent intent = new Intent(Book_AppointmnetActivity.this, Clinic_ProfileActivity.class);
            startActivity(intent);
        });

        // Set up the Spinner for selecting pets
        ArrayAdapter<String> petAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Dog", "Cat"});
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        petSpinner.setAdapter(petAdapter);

        // Set up the Spinner for selecting services
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new String[]{"Vaccination", "Grooming", "Checkup", "Surgery"});
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);
    }
}
