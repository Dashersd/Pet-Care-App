package com.example.petcareapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edit_petActivity extends AppCompatActivity {

    private EditText editText4, editText5, editText6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_pet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the EditTexts
        editText4 = findViewById(R.id.editText4); // For pet name
        editText5 = findViewById(R.id.editText5); // For pet birthdate
        editText6 = findViewById(R.id.editText6); // For pet breed

        // Fetch pet information from Firebase Realtime Database
        fetchPetInformation();
    }

    private void fetchPetInformation() {
        // Get Firebase references
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Access the pet information node
        databaseReference.child(userId).child("petInformation").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Assuming a single pet's information for simplicity
                    for (DataSnapshot petSnapshot : snapshot.getChildren()) {
                        // Retrieve pet information
                        String petName = petSnapshot.child("petName").getValue(String.class);
                        String petBirth = petSnapshot.child("dob").getValue(String.class); // "dob" stands for date of birth
                        String petBreed = petSnapshot.child("breed").getValue(String.class);

                        // Set data to EditTexts
                        editText4.setText(petName);
                        editText5.setText(petBirth);
                        editText6.setText(petBreed);

                        break; // Exit loop after fetching the first pet (if multiple pets exist)
                    }
                } else {
                    Toast.makeText(edit_petActivity.this, "No pet information found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle database errors
                Toast.makeText(edit_petActivity.this, "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
