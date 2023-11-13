package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private TextView doctorStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;
    private String userStatus = ""; // Global variable for user status

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_welcome_screen);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        doctorStatus = findViewById(R.id.doctorStatus);
        logOutButton = findViewById(R.id.doctorLogOut);

        checkUserStatus();

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

    private void checkUserInCollection(String userId, String collection, String status) {
        fstore.collection(collection).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    userStatus = status; // Set the global userStatus variable
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
