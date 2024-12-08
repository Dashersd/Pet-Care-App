package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Clinic_ProfileActivity extends AppCompatActivity {

    // Declare UI elements
    private TextView clinicNameTextView, clinicEmailTextView;
    private Button bookAppointmentButton;

    // Firebase instances
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clinic_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase instances
        databaseReference = FirebaseDatabase.getInstance().getReference("Clinics");

        // Initialize views
        clinicNameTextView = findViewById(R.id.clinic_name);
        clinicEmailTextView = findViewById(R.id.clinic_email);
        bookAppointmentButton = findViewById(R.id.button20);

        String clinicName = getIntent().getStringExtra("clinic_name");
        fetchClinicDetails(clinicName);

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Clinic_ProfileActivity.this, Clinic_SelectionActivity.class);
            startActivity(intent);
        });

        bookAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(Clinic_ProfileActivity.this, Book_AppointmnetActivity.class);
            intent.putExtra("clinic_name", clinicNameTextView.getText().toString().trim());
            startActivity(intent);
        });
    }

    private void fetchClinicDetails(String clinicName) {
        databaseReference.orderByChild("clinicName").equalTo(clinicName)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Clinic clinic = snapshot.getValue(Clinic.class);
                                if (clinic != null) {
                                    clinicNameTextView.setText(clinic.getClinicName());
                                    clinicEmailTextView.setText(clinic.getEmail());
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(Clinic_ProfileActivity.this,
                                    "No clinic found for this user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Clinic_ProfileActivity.this,
                                "Failed to fetch clinic details.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
