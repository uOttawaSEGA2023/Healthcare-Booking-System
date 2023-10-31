package com.example.project10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PatientWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private TextView patientStatus;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_welcome_screen);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        patientStatus = findViewById(R.id.patientStatus);
        logOutButton = findViewById(R.id.patientLogOut);

        checkUserStatus();

        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainIntent = new Intent(PatientWelcomeActivity.this, MainActivity.class);
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
            patientStatus.setText("Status: Not logged in");
        }
    }

    private void checkUserInCollection(String userId, String collection, String status) {
        fstore.collection(collection).document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                if(status.equals("Rejected")){
                    patientStatus.setText("Status: " + status + ", Email admin@gmail.com");
                }else{
                    patientStatus.setText("Status: " + status);
                }
            }
        });
    }
}
