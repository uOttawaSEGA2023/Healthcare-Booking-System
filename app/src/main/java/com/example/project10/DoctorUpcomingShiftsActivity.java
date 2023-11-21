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

public class DoctorUpcomingShiftsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DoctorShiftViewAdapter adapter;
    private Button backButton;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_doctor_upcoming_shifts);

        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorShiftViewAdapter();
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadUpcomingAppointments();

        backButton = findViewById(R.id.backRequestButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DoctorUpcomingShiftsActivity.this, DoctorWelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadUpcomingAppointments() {
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("upcoming shifts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Shift> shifts = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Shift shift = document.toObject(Shift.class);
                            shift.setDocumentId(document.getId());
                            shifts.add(shift);
                        }
                        adapter.setShifts(shifts);
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
