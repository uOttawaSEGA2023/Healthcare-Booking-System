package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import android.widget.Toast;
import java.time.LocalDateTime;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.widget.CheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private TextView doctorStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String userStatus = "";

    private CheckBox autoAcceptCheckbox;


    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_welcome_screen);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        doctorStatus = findViewById(R.id.doctorStatus);
        logOutButton = findViewById(R.id.doctorLogOut);
        autoAcceptCheckbox = findViewById(R.id.autoAcceptCheckbox);

        db = FirebaseFirestore.getInstance();

        checkUserStatus();

        checkAndMovePastAppointments();
        checkAndDeletePastShifts();

        autoAcceptCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                moveAllAppointmentsToUpcoming();
            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainIntent = new Intent(DoctorWelcomeActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        Button btnUpcomingAppointments = findViewById(R.id.button_doctor_upcomingApp);
        btnUpcomingAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("Accepted")) {
                    Intent intent = new Intent(DoctorWelcomeActivity.this, DoctorUpcomingAppointmentsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnPastAppointments = findViewById(R.id.button_doctor_pastApp);
        btnPastAppointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("Accepted")) {
                    Intent intent = new Intent(DoctorWelcomeActivity.this, DoctorPastAppointmentsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnUpcomingShifts = findViewById(R.id.button_doctor_upcomingShifts);
        btnUpcomingShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("Accepted")) {
                    Intent intent = new Intent(DoctorWelcomeActivity.this, DoctorUpcomingShiftsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnAppointmentRequests = findViewById(R.id.button_doctor_app_requests);
        btnAppointmentRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("Accepted")) {
                    Intent intent = new Intent(DoctorWelcomeActivity.this, DoctorAppointmentRequestsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnCreateAppointment = findViewById(R.id.button_doctor_createApp);
        btnCreateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userStatus.equals("Accepted")) {
                    Intent intent = new Intent(DoctorWelcomeActivity.this, DoctorCreateAppointmentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(DoctorWelcomeActivity.this, "This feature is only available for accepted users", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            checkUserInCollection(userId, "pending users", "Pending");
            checkUserInCollection(userId, "accepted doctors", "Accepted");
            checkUserInCollection(userId, "rejected users", "Rejected");
        } else {
            doctorStatus.setText("Status: Not logged in");
        }
    }



    private void checkAndMovePastAppointments() {
        Calendar now = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        db.collection("accepted doctors")
                .document(getCurrentUserId())
                .collection("upcoming appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            String documentId = document.getId(); // Get the document ID
                            if (isPastAppointment(appointment, now, dateFormat, timeFormat)) {
                                moveAppointmentToPast(appointment, documentId); // Pass the document ID
                            }
                        }
                    }
                });
    }


    private void moveAllAppointmentsToUpcoming() {
        String currentUserId = getCurrentUserId();

        db.collection("accepted doctors")
                .document(getCurrentUserId())
                .collection("appointment requests") // Assuming this is your collection name
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Appointment appointment = document.toObject(Appointment.class);
                            String documentId = document.getId(); // Get the document ID

                            db.collection("accepted doctors")
                                    .document(currentUserId)
                                    .collection("upcoming appointments")
                                    .document(documentId)  // Use the existing document ID
                                    .set(appointment)      // Set the appointment data
                                    .addOnSuccessListener(documentReference -> {
                                        deleteAppointmentFromRequests(documentId);
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(DoctorWelcomeActivity.this, "Failed to move appointment: " + documentId, Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(DoctorWelcomeActivity.this, "Failed to fetch appointment requests", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteAppointmentFromRequests(String documentId) {
        db.collection("accepted doctors")
                .document(getCurrentUserId())
                .collection("appointment requests")
                .document(documentId)
                .delete();
    }



    private void moveAppointmentToPast(Appointment appointment, String documentId) {
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("past appointments")
                .document(documentId)
                .set(appointment)
                .addOnSuccessListener(documentReference -> {
                    deleteAppointmentFromUpcoming(documentId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DoctorWelcomeActivity.this, "Failed to move appointment", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteAppointmentFromUpcoming(String documentId) {
        String currentUserId = getCurrentUserId();
        db.collection("accepted doctors")
                .document(currentUserId)
                .collection("upcoming appointments")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }

    private void deleteShiftFromUpcoming(String documentId) {
        db.collection("accepted doctors")
                .document(getCurrentUserId())
                .collection("upcoming shifts")
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


    private void checkAndDeletePastShifts() {
        Calendar now = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        db.collection("accepted doctors")
                .document(getCurrentUserId())
                .collection("upcoming shifts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Shift shift = document.toObject(Shift.class);
                            String documentId = document.getId();
                            if (isPastShift(shift, now, dateFormat, timeFormat)) {
                                deleteShiftFromUpcoming(documentId);
                            }
                        }
                    }
                });
    }

    private boolean isPastShift(Shift shift, Calendar now, SimpleDateFormat dateFormat, SimpleDateFormat timeFormat) {
        try {
            Date shiftDate = dateFormat.parse(shift.getDate());
            Date shiftStartTime = timeFormat.parse(shift.getStartTime());
            Date shiftEndTime = timeFormat.parse(shift.getEndTime());

            Calendar shiftDateTimeStart = Calendar.getInstance();
            Calendar shiftDateTimeEnd = Calendar.getInstance();
            if (shiftDate != null && shiftStartTime != null && shiftEndTime != null) {
                shiftDateTimeStart.setTime(shiftDate);
                shiftDateTimeStart.set(Calendar.HOUR_OF_DAY, shiftStartTime.getHours());
                shiftDateTimeStart.set(Calendar.MINUTE, shiftStartTime.getMinutes());
                shiftDateTimeEnd.setTime(shiftDate);
                shiftDateTimeEnd.set(Calendar.HOUR_OF_DAY, shiftEndTime.getHours());
                shiftDateTimeEnd.set(Calendar.MINUTE, shiftEndTime.getMinutes());
            }

            return shiftDateTimeEnd.before(now);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }



    private void checkUserInCollection(String userId, String collection, String status) {
        fstore.collection(collection).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    userStatus = status;
                    if(status.equals("Rejected")){
                        doctorStatus.setText("Status: " + status + "\nEmail admin@gmail.com");
                    } else {
                        doctorStatus.setText("Status: " + status);
                    }
                }
            }
        });
    }
}
