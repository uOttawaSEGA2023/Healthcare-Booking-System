package com.example.project10;
import java.util.Map;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;


public class PatientRegistrationActivity extends AppCompatActivity {

    private EditText firstNameInput, lastName, emailAddress, homeAddress, userPassword, phoneNumber, healthCardNumber;
    private Button submitButton, backButton;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        fstore= FirebaseFirestore.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // Initialize UI components
        firstNameInput = findViewById(R.id.firstNameInput);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.emailAddress);
        homeAddress = findViewById(R.id.homeAddress);
        userPassword = findViewById(R.id.userPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        healthCardNumber = findViewById(R.id.healthCardNumber);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);


        // Set onClick listener for submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstNameInput.getText().toString().trim();
                String last = lastName.getText().toString().trim();
                String email = emailAddress.getText().toString().trim();
                String address = homeAddress.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                String healthCard = healthCardNumber.getText().toString().trim();

                if (validateFields(first, last, email, address, password, phone, healthCard)) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userID = user != null ? user.getUid() : "";

                                        // Document references for both collections
                                        DocumentReference userDocRef = fstore.collection("users").document(userID);
                                        DocumentReference statusDocRef = fstore.collection("non processed IDs").document(userID);

                                        // User information
                                        Map<String, Object> patient = new HashMap<>();
                                        patient.put("role", "patient");
                                        patient.put("firstName", first);
                                        patient.put("lastName", last);
                                        patient.put("email", email);
                                        patient.put("address", address);
                                        patient.put("phone", phone);
                                        patient.put("healthCardNumber", healthCard);

                                        // Status information
                                        Map<String, Object> status = new HashMap<>();
                                        status.put("status", "pending");

                                        // Add user info to "users" collection
                                        userDocRef.set(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Add status to "non processed IDs" collection
                                                    statusDocRef.set(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(PatientRegistrationActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(PatientRegistrationActivity.this, PatientWelcomeActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                Toast.makeText(PatientRegistrationActivity.this, "Error in saving status", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(PatientRegistrationActivity.this, "Error in saving user data", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(PatientRegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to RoleActivity
                Intent roleIntent = new Intent(PatientRegistrationActivity.this, RoleActivity.class);
                startActivity(roleIntent);
            }
        });

    }

    private boolean validateFields(String first, String last, String email, String address, String password, String phone, String healthCard) {
        int fieldCount = 0;

        if (first.isEmpty()) fieldCount++;
        if (last.isEmpty()) fieldCount++;
        if (email.isEmpty()) fieldCount++;
        if (address.isEmpty()) fieldCount++;
        if (password.isEmpty()) fieldCount++;
        if (phone.isEmpty()) fieldCount++;
        if (healthCard.isEmpty()) fieldCount++;

        if (fieldCount > 0) {
            Toast.makeText(PatientRegistrationActivity.this, "Please fill in " + fieldCount + " missing field(s)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidName(first) || !isValidName(last)) {
            Toast.makeText(PatientRegistrationActivity.this, "Names should only contain alphabets", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid password (6 or more characters)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid phone number (10 Digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidCard(healthCard)) {
            Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid health card (10 Digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidAddress(address)) {
            Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private boolean isValidPhone(String phone) {
        return phone.length() == 10 && phone.matches("[0-9]+");
    }

    private boolean isValidCard(String healthCard) {
        return healthCard.length() == 10 && healthCard.matches("[0-9]+");
    }

    private boolean isValidAddress(String address) {
        if (address.length() < 5 || address.length() > 100) {
            return false;
        }
        String addressPattern = "^[a-zA-Z0-9 ,.-]+$";
        return address.matches(addressPattern);
    }
}


