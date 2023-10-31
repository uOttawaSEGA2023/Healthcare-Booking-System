package com.example.project10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class AdminWelcomeActivity extends AppCompatActivity {
    private Button logOutButton;
    private Button registationRequestsButton;

    private Button rejectedUsersButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome_screen);
        mAuth = FirebaseAuth.getInstance();


        logOutButton = findViewById(R.id.logOut);
        registationRequestsButton = findViewById(R.id.registrationRequests);

        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(AdminWelcomeActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        });
        registationRequestsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(AdminWelcomeActivity.this, AdminRegistrationRequests.class);
                startActivity(mainIntent);

            }
        });
    }
}
