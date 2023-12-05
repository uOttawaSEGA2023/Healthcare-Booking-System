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

public class PatientPastAppointments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PatientPastAppointmentsAdapter adapter;
    private Button backButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_past_appointments);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientPastAppointmentsAdapter();
        recyclerView.setAdapter(adapter);

        loadPastAppointments();

        backButton = findViewById(R.id.backPatientPastAppButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(PatientPastAppointments.this, PatientWelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadPastAppointments() {
        String currentUserId = getCurrentUserId();
        db.collection("accepted patients")
                .document(currentUserId)
                .collection("past appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointments.add(appointment);
                        }
                        adapter.setAppointments(appointments);
                    } else {
                        Toast.makeText(PatientPastAppointments.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }
}

