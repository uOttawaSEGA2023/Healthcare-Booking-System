package com.example.project10;

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

public class DoctorAppointmentRequestsActivity extends AppCompatActivity implements DoctorAppointmentRequestAdapter.OnAppointmentActionListener {

    private RecyclerView recyclerView;
    private DoctorAppointmentRequestAdapter adapter;
    private Button backButton, deniedRequestsButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment_requests);

        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorAppointmentRequestAdapter(this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadAppointmentRequests();

        backButton = findViewById(R.id.backRequestButton);
        backButton.setOnClickListener(v -> finish());

        deniedRequestsButton = findViewById(R.id.deniedRequestsButton);
        deniedRequestsButton.setOnClickListener(v -> finish()); // Update this if it should have a different behavior
    }

    private void loadAppointmentRequests() {
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("appointment requests")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Appointment> appointments = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            appointment.setDocumentId(document.getId()); // Store the document ID
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

    @Override
    public void onAccept(Appointment appointment) {
        moveAppointmentToUpcoming(appointment);
    }

    @Override
    public void onDeny(Appointment appointment) {
        deleteAppointmentRequest(appointment);
    }

    private void moveAppointmentToUpcoming(Appointment appointment) {
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("upcoming appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    deleteAppointmentRequest(appointment);
                    loadAppointmentRequests(); // Refresh list after accepting
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to move appointment", Toast.LENGTH_SHORT).show());
    }

    private void deleteAppointmentRequest(Appointment appointment) {
        String currentUserId = getCurrentUserId();
        String appointmentId = appointment.getDocumentId(); // Get the stored document ID
        if (appointmentId != null && !appointmentId.isEmpty()) {
            db.collection("accepted doctors")
                    .document(currentUserId)
                    .collection("appointment requests")
                    .document(appointmentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Appointment deleted", Toast.LENGTH_SHORT).show();
                        loadAppointmentRequests(); // Refresh list after deleting
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error deleting appointment", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Error: No document ID found", Toast.LENGTH_SHORT).show();
        }
    }

}
