package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleActivity extends AppCompatActivity {

    private Button registerAsPatientButton;
    private Button registerAsDoctorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

        // Initialize UI components
        registerAsPatientButton = findViewById(R.id.registerAsPatient);
        registerAsDoctorButton = findViewById(R.id.registerAsDoctor);

        // Set onClick listeners
        registerAsPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent patientRegistrationIntent = new Intent(RoleActivity.this, PatientRegistrationActivity.class);
                startActivity(patientRegistrationIntent);
            }
        });

        registerAsDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doctorRegistrationIntent = new Intent(RoleActivity.this, DoctorRegistrationActivity.class);
                startActivity(doctorRegistrationIntent);
            }
        });
    }
}
