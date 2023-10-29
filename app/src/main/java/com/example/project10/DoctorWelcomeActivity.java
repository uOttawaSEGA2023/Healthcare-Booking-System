package com.example.project10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class DoctorWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_welcome_screen);
        mAuth = FirebaseAuth.getInstance();

        logOutButton = findViewById(R.id.patientLogOut);

        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent mainIntent = new Intent(DoctorWelcomeActivity.this, MainActivity.class);
                startActivity(mainIntent);

            }
        });

    }
}