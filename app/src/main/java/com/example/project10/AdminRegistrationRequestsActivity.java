package com.example.project10;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminRegistrationRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> userDetailsList;
    private ArrayList<String> userIds; // List to store user IDs
    private Button backButton, deniedRequestsButton;
    private UserAdapter adapter;
    private FirebaseFirestore firestore;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_requests);

        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userDetailsList = new ArrayList<>();
        userIds = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("pending users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                userDetailsList.clear();
                userIds.clear();
                for (DocumentSnapshot snapshot : documentSnapshots) {
                    if (snapshot.exists()) {
                        String userId = snapshot.getId();
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");
                        String fullName = firstName + " " + lastName;

                        String role = snapshot.getString("role");
                        String phone = snapshot.getString("phone");
                        String email = snapshot.getString("email");
                        String healthCardNumber = snapshot.getString("healthCardNumber");
                        String employeeNumber = snapshot.getString("employeeNumber");
                        String specialties = snapshot.getString("specialties");

                        String details = "";
                        if ("patient".equals(role)) {
                            details = healthCardNumber;
                        } else if ("doctor".equals(role)) {
                            details = employeeNumber;
                            details += ", " + specialties;
                        }

                        userDetailsList.add(fullName + ", " + role + ", " + email + ", " + phone + ", " + details);
                        userIds.add(userId);
                    }
                }

                adapter = new UserAdapter(getApplicationContext(), userDetailsList, userIds);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            }
        });

        backButton = findViewById(R.id.backRequestButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRegistrationRequestsActivity.this, AdminWelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deniedRequestsButton = findViewById(R.id.deniedRequestsButton);
        deniedRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRegistrationRequestsActivity.this, AdminDeniedRequestsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

