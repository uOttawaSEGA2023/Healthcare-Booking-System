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

public class DoctorPastAppointmentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorPastAppointmentViewAdapter adapter;
    private Button backButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_doctor_past_appointments);

        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorPastAppointmentViewAdapter();
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadPastAppointments(); // Renamed method

        backButton = findViewById(R.id.backRequestButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorPastAppointmentsActivity.this, DoctorWelcomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadPastAppointments() { // Renamed and updated method
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("past appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setDocumentId(document.getId());
                            appointments.add(appointment);
                        }
                        adapter.setAppointments(appointments);
                    } else {
                        Toast.makeText(this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }
}

