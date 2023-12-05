package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PatientRequestAppointmentWithSpecificDoctor extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientRequestAppointmentWithSpecificDoctorAdapter adapter;
    private Button backButton;
    private String doctorDocumentID;
    private String currentUserId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_request_appointment_with_specific_doctor);
        mAuth = FirebaseAuth.getInstance();

        doctorDocumentID = getIntent().getStringExtra("DOCTOR_DOCUMENT_ID");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        recyclerView = findViewById(R.id.recyclerViewUpcomingShifts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientRequestAppointmentWithSpecificDoctorAdapter(doctorDocumentID, currentUserId);
        recyclerView.setAdapter(adapter);

        backButton = findViewById(R.id.backButtonAppReqPatient);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientRequestAppointmentWithSpecificDoctor.this, PatientRequestAppointment.class);
            startActivity(intent);
            finish();
        });

        if (doctorDocumentID != null && !doctorDocumentID.isEmpty()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("accepted doctors")
                    .document(doctorDocumentID)
                    .collection("upcoming shifts")
                    .get()
                    .addOnCompleteListener(shiftTask -> {
                        if (shiftTask.isSuccessful()) {
                            List<Shift> shifts = new ArrayList<>();
                            for (QueryDocumentSnapshot shiftDocument : shiftTask.getResult()) {
                                Shift shift = shiftDocument.toObject(Shift.class);
                                shifts.add(shift);
                            }
                            adapter.setShiftsAsAppointments(shifts);
                        } else {
                            Toast.makeText(PatientRequestAppointmentWithSpecificDoctor.this, "Error loading shifts", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Doctor ID is not available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}


