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

public class license_detailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_license_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Set up leftArrow to open DashboardActivity
        ImageView leftArrow = findViewById(R.id.leftArrow);
        leftArrow.setOnClickListener(v -> {
            Intent intent = new Intent(license_detailsActivity.this, pet_profileActivity.class);
            startActivity(intent);
        });
        // Set up textView47 to open EditPetActivity
        TextView textView47 = findViewById(R.id.textView47);
        textView47.setOnClickListener(v -> {
            Intent intent = new Intent(license_detailsActivity.this, edit_licenseActivity.class);
            startActivity(intent);
        });
    }
}