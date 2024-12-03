package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class pet_profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_profile);

        // Apply system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up button to open PetDetailsActivity
        Button buttonPetDetails = findViewById(R.id.buttonPetDetails);
        buttonPetDetails.setOnClickListener(v -> {
            Intent intent = new Intent(pet_profileActivity.this, pet_detailsActivity.class);
            startActivity(intent);
        });

        // Set up leftArrow to open DashboardActivity
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(v -> {
            Intent intent = new Intent(pet_profileActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}
