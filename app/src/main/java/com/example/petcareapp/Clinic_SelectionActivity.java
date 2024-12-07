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

public class Clinic_SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic_selection);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.imageView36);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the AppoinmentActivity
                Intent intent = new Intent(Clinic_SelectionActivity.this, AppoinmentActivity.class);
                startActivity(intent);
            }
        });

        Button viewProfileButton = findViewById(R.id.view_profile_button_1);
        viewProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the Clinic_ProfileActivity
                Intent intent = new Intent(Clinic_SelectionActivity.this, Clinic_ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button bookAppointmentButton = findViewById(R.id.book_appointment_button_1);
        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clinic name (hardcoded or fetched dynamically)
                String clinicName = "Happy Paws Veterinary Clinic"; // Replace with dynamic value if needed

                // Create an Intent to navigate to the Book_Appointment2Activity
                Intent intent = new Intent(Clinic_SelectionActivity.this, Book_AppointmnetActivity.class);

                // Pass the clinic name to the next activity
                intent.putExtra("clinic_name", clinicName);

                // Start the activity
                startActivity(intent);
            }
        });
    }
}
