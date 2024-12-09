package com.example.petcareapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Book_Appointment2Activity extends AppCompatActivity {

    private String selectedSlot = null; // To store the selected slot
    private String selectedDate = null; // To store the selected date
    private String clinicName = null; // To store the clinic name
    private String petName = null; // To store the selected pet
    private String serviceType = null; // To store the selected service
    private String appointmentReason = null; // To store the reason for appointment
    private DatabaseReference databaseReference;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment2);

        // Retrieve the current user's ID from Firebase Authentication
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            Toast.makeText(this, "Error: User not logged in.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Retrieve the clinic name from the Intent
        clinicName = getIntent().getStringExtra("clinic_name");
        if (clinicName == null || clinicName.isEmpty()) {
            Toast.makeText(this, "Error: Clinic name not provided.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database References
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);

        // Fetch appointment details from the user's appointment data
        fetchAppointmentDetails();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize slot views, calendar view, and button
        TextView slot8am = findViewById(R.id.slot_8am);
        TextView slot830am = findViewById(R.id.slot_830am);
        TextView slot10am = findViewById(R.id.slot_10am);
        CalendarView calendarView = findViewById(R.id.calendar_view);
        Button makeAppointmentButton = findViewById(R.id.make_appointment_button);

        // Slot click listener
        slot8am.setOnClickListener(view -> selectSingleSlot(slot8am));
        slot830am.setOnClickListener(view -> selectSingleSlot(slot830am));
        slot10am.setOnClickListener(view -> selectSingleSlot(slot10am));

        // Date selection listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        // Button click listener to show confirmation dialog
        makeAppointmentButton.setOnClickListener(view -> {
            if (selectedSlot != null && selectedDate != null) {
                showConfirmationDialog();
            } else {
                Toast.makeText(this, "Please select both a slot and a date.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectSingleSlot(TextView selectedSlotView) {
        resetSlotSelection();
        selectedSlot = selectedSlotView.getText().toString();
        selectedSlotView.setBackgroundColor(Color.LTGRAY);
    }

    private void resetSlotSelection() {
        findViewById(R.id.slot_8am).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.slot_830am).setBackgroundColor(Color.TRANSPARENT);
        findViewById(R.id.slot_10am).setBackgroundColor(Color.TRANSPARENT);
    }

    private void fetchAppointmentDetails() {
        databaseReference.child("appointmentDetails").get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        petName = dataSnapshot.child("selectedPet").getValue(String.class);
                        serviceType = dataSnapshot.child("selectedService").getValue(String.class);
                        appointmentReason = dataSnapshot.child("reason").getValue(String.class);
                    } else {
                        Toast.makeText(this, "No appointment details found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to fetch details: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Appointment")
                .setMessage("Selected slot: " + selectedSlot +
                        "\nSelected date: " + selectedDate +
                        "\nClinic: " + clinicName +
                        "\nPet: " + petName +
                        "\nService: " + serviceType +
                        "\nReason: " + appointmentReason)
                .setPositiveButton("Confirm", (dialog, which) -> saveAppointmentToDatabase())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void saveAppointmentToDatabase() {
       DatabaseReference userAppointmentsRef = databaseReference.child("appointments");
        String appointmentId = userAppointmentsRef.push().getKey();
        Map<String, Object> appointmentData = new HashMap<>();
        appointmentData.put("slot", selectedSlot);
        appointmentData.put("date", selectedDate);
        appointmentData.put("clinic_name", clinicName);
        appointmentData.put("pet", petName);
        appointmentData.put("service", serviceType);
        appointmentData.put("reason", appointmentReason);
        appointmentData.put("status", "confirmed");

        if (appointmentId != null) {
            userAppointmentsRef.child(appointmentId).setValue(appointmentData);
        }
    }
}
