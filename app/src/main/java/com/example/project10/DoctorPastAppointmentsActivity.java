
package com.example.project10;

import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.firestore.FirebaseFirestore;

public class DoctorPastAppointmentsActivity extends AppCompatActivity {

    private Button logOutButton, backButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_past_appointments);
        logOutButton = findViewById(R.id.doctorLogOut);
        backButton = findViewById(R.id.doctorPastAppBackButton);

        mAuth = FirebaseAuth.getInstance();


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainIntent = new Intent(DoctorPastAppointmentsActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(DoctorPastAppointmentsActivity.this, DoctorWelcomeActivity.class);
                startActivity(mainIntent);
            }
        });
    }

}
