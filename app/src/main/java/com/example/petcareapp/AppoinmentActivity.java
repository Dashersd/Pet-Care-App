package com.example.petcareapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppoinmentActivity extends AppCompatActivity {

    private TextView selectedDayTextView; // To store the currently selected day

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment);

        GridLayout calendarGrid = findViewById(R.id.calendar_grid);
        TextView monthYearText = findViewById(R.id.month_year_text);
        ImageView addAppointmentButton = findViewById(R.id.imageView35);

        // Example for setting the month and year text
        monthYearText.setText("September");

        // Days of the month for September (example)
        String[] days = {
                "", "", "", "", "", "1", "2",
                "3", "4", "5", "6", "7", "8", "9",
                "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23",
                "24", "25", "26", "27", "28", "29", "30"
        };

        // Populate the calendar dynamically
        for (String day : days) {
            TextView dayTextView = new TextView(this);
            dayTextView.setText(day);
            dayTextView.setGravity(Gravity.CENTER);
            dayTextView.setTextColor(Color.BLACK);
            dayTextView.setPadding(18, 16, 16, 16);

            if (!day.isEmpty()) {
                // Set OnClickListener for day selection
                dayTextView.setOnClickListener(v -> handleDaySelection(dayTextView));
            }

            calendarGrid.addView(dayTextView);
        }

        // Open a new activity when the add button is clicked
        addAppointmentButton.setOnClickListener(v -> {
            Intent intent = new Intent(AppoinmentActivity.this, Clinic_SelectionActivity.class);
            startActivity(intent);
        });
    }

    // Handle the day selection
    private void handleDaySelection(TextView dayTextView) {
        if (selectedDayTextView != null) {
            // Clear background from the previously selected day
            selectedDayTextView.setBackgroundResource(0);
        }

        // Highlight the selected day
        dayTextView.setBackgroundResource(R.drawable.circle_background);
        selectedDayTextView = dayTextView;
    }
}
