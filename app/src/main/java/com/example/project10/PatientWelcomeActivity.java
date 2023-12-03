package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PatientWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private Button patientRequestAppointmentButton;
    private Button patientUpcomingAppointmentButton;
    private Button patientPastAppointmentButton;
    private TextView patientStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userStatus = ""; // Added variable to store user status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_welcome_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        patientStatus = findViewById(R.id.patientStatus);
        logOutButton = findViewById(R.id.patientLogOut);
        patientRequestAppointmentButton = findViewById(R.id.patientRequestAppointmentButton);
        patientUpcomingAppointmentButton = findViewById(R.id.patientUpcomingAppointmentButton);
        patientPastAppointmentButton = findViewById(R.id.patientPastAppointmentButton);

        checkUserStatus();
        checkAndMovePastAppointments();
        setButtonListeners();
    }

    private void setButtonListeners() {
        logOutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent mainIntent = new Intent(PatientWelcomeActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });

        patientRequestAppointmentButton.setOnClickListener(v -> {
            performActionIfAccepted(PatientRequestAppointment.class);
        });

        patientUpcomingAppointmentButton.setOnClickListener(v -> {
            performActionIfAccepted(PatientUpcomingAppointments.class);
        });

        patientPastAppointmentButton.setOnClickListener(v -> {
            performActionIfAccepted(PatientPastAppointments.class);
        });
    }

    private void performActionIfAccepted(Class<?> activityClass) {
        if ("Accepted".equals(userStatus)) {
            Intent intent = new Intent(PatientWelcomeActivity.this, activityClass);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(PatientWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            checkUserInCollection(userId, "pending users", "Pending");
            checkUserInCollection(userId, "accepted patients", "Accepted");
            checkUserInCollection(userId, "rejected users", "Rejected");
        } else {
            patientStatus.setText("Status: Not logged in");
        }
    }

    private void checkUserInCollection(String userId, String collection, String status) {
        db.collection(collection).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                userStatus = status; // Set the user status here
                if ("Rejected".equals(status)) {
                    patientStatus.setText("Status: " + status + ", Email admin@gmail.com");
                } else {
                    patientStatus.setText("Status: " + status);
                }
            }
        });
    }

    private void checkAndMovePastAppointments() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        db.collection("accepted patients")
                .document(getCurrentUserId())
                .collection("upcoming appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            String documentId = document.getId();
                            if (isPastAppointment(appointment, now, dateFormat, timeFormat)) {
                                moveAppointmentToPast(appointment, documentId);
                            }
                        }
                    }
                });
    }

    private void moveAppointmentToPast(Appointment appointment, String documentId) {
        String currentUserId = getCurrentUserId();

        db.collection("accepted patients")
                .document(currentUserId)
                .collection("past appointments")
                .document(documentId)
                .set(appointment)
                .addOnSuccessListener(documentReference -> {
                    deleteAppointmentFromUpcoming(documentId);
                })
                .addOnFailureListener(e -> {
                });
    }

    private void deleteAppointmentFromUpcoming(String documentId) {
        String currentUserId = getCurrentUserId();
        db.collection("accepted patients")
                .document(currentUserId)
                .collection("upcoming appointments")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }


    private boolean isPastAppointment(Appointment appointment, Calendar now, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat) {
        try {
            Date appointmentDate = dateFormat.parse(appointment.getAppDate());
            Date appointmentTime = timeFormat.parse(appointment.getAppStartTime());

            Calendar appointmentDateTime = Calendar.getInstance();
            if (appointmentDate != null && appointmentTime != null) {
                appointmentDateTime.setTime(appointmentDate);
                Calendar appointmentTimeCal = Calendar.getInstance();
                appointmentTimeCal.setTime(appointmentTime);
                appointmentDateTime.set(Calendar.HOUR_OF_DAY, appointmentTimeCal.get(Calendar.HOUR_OF_DAY));
                appointmentDateTime.set(Calendar.MINUTE, appointmentTimeCal.get(Calendar.MINUTE));
            }

            return appointmentDateTime.before(now);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }
}

