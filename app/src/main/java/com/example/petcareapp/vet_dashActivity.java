package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class vet_dashActivity extends AppCompatActivity {

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
        // Initialize and set OnClickListener for the profile view
        View profileView = findViewById(R.id.view3);
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Profile Activity
                Intent intent = new Intent(vet_dashActivity.this, vet_profileActivity.class);
                startActivity(intent);
            }
        });
    }
}