package com.example.petcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth; // Declare FirebaseAuth instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

//           //  Initialize Firebase Authentication
//            mAuth = FirebaseAuth.getInstance();
//
//            // Check if the user is already signed in
//            FirebaseUser user = mAuth.getCurrentUser();
//            if (user != null) {
//                // User is signed in, navigate to DashboardActivity
//                Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
//                startActivity(intent);
//                finish(); // Close this activity to prevent returning back
//                return;
//            }

        // Write a message to the Firebase Realtime Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Firebase Connected!");

        // Set up the login button
        Button loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Pet_OwnerLogInActivity.class);
            startActivity(intent);
        });

        // Set up the sign-up button
        Button signButton = findViewById(R.id.button2);
        signButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}
