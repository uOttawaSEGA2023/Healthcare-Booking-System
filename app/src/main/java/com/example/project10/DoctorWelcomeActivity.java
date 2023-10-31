package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private TextView doctorStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

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
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            checkUserInCollection(userId, "pending users", "Pending");
            checkUserInCollection(userId, "accepted users", "Accepted");
            checkUserInCollection(userId, "rejected users", "Rejected");
        } else {
            doctorStatus.setText("Status: Not logged in");
        }
    }

    private void checkUserInCollection(String userId, String collection, String status) {
        fstore.collection(collection).document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    doctorStatus.setText("Status: " + status);
                }
            }
        });
    }
}
