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

public class DoctorRegistrationActivity extends AppCompatActivity {

    private EditText firstNameInput, lastName, emailAddress, homeAddress, userPassword, phoneNumber, employeeNumber, specialtiesInput;
    private Button submitButton2, backButton2;
    FirebaseAuth mAuth;
    FirebaseFirestore pendingFirestore;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        pendingFirestore = FirebaseFirestore.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);
        mAuth = FirebaseAuth.getInstance();
        // Initialize UI components
        firstNameInput = findViewById(R.id.firstNameInput);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.emailAddress);
        homeAddress = findViewById(R.id.homeAddress);
        userPassword = findViewById(R.id.userPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        employeeNumber = findViewById(R.id.employeeNumber);
        specialtiesInput = findViewById(R.id.Specialties); // Renamed for clarity
        submitButton2 = findViewById(R.id.submitButton2);
        backButton2 = findViewById(R.id.backButton2);

// Set onClick listener for submit button
        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first = firstNameInput.getText().toString().trim();
                String last = lastName.getText().toString().trim();
                String email = emailAddress.getText().toString().trim();
                String address = homeAddress.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                String phone = phoneNumber.getText().toString().trim();
                String empNumber = employeeNumber.getText().toString().trim();
                String docSpecialties = specialtiesInput.getText().toString().trim();

                // Validate all fields
                if (!validateFields(first, last, email, address, password, phone, empNumber, docSpecialties)) {
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userID = user != null ? user.getUid() : "";

                                    // Prepare the doctor data
                                    Map<String, Object> doctor = new HashMap<>();
                                    doctor.put("role", "doctor");
                                    doctor.put("firstName", first);
                                    doctor.put("lastName", last);
                                    doctor.put("email", email);
                                    doctor.put("address", address);
                                    doctor.put("phone", phone);
                                    doctor.put("employeeNumber", empNumber);
                                    doctor.put("specialties", docSpecialties);

                                    DocumentReference userDocRefPending = pendingFirestore.collection("pending users").document(userID);
                                    DocumentReference userDocRefUsers = pendingFirestore.collection("users").document(userID);

                                    userDocRefPending.set(doctor)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        userDocRefUsers.set(doctor)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(DoctorRegistrationActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                                                                            Intent mainIntent = new Intent(DoctorRegistrationActivity.this, MainActivity.class);
                                                                            startActivity(mainIntent);
                                                                            finish();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(DoctorRegistrationActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to RoleActivity
                Intent roleIntent = new Intent(DoctorRegistrationActivity.this, RoleActivity.class);
                startActivity(roleIntent);
                finish();
            }
        });
    }

    private boolean validateFields(String first, String last, String email, String address, String password, String phone, String empNumber, String docSpecialties) {
        int fieldCount = 0;

        if (first.isEmpty()) fieldCount++;
        if (last.isEmpty()) fieldCount++;
        if (email.isEmpty()) fieldCount++;
        if (address.isEmpty()) fieldCount++;
        if (password.isEmpty()) fieldCount++;
        if (phone.isEmpty()) fieldCount++;
        if (empNumber.isEmpty()) fieldCount++;
        if (docSpecialties.isEmpty()) fieldCount++;

        if (fieldCount > 0) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please fill in " + fieldCount + " missing field(s)", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!isValidName(first) || !isValidName(last)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Names should only contain alphabets", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid password (6 or more characters)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPhone(phone)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid phone number (10 Digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmployee(empNumber)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid employee number (10 Digits)", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidAddress(address)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isValidSpecialty(docSpecialties)) {
            Toast.makeText(DoctorRegistrationActivity.this, "Please enter 1 or more valid specialties", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
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

    private boolean isValidEmployee(String empNumber) {
        return empNumber.length() == 10 && empNumber.matches("[0-9]+");
    }

    private boolean isValidAddress(String address) {
        if (address.length() < 5 || address.length() > 100) {
            return false;
        }
        String addressPattern = "^[a-zA-Z0-9 ,.-]+$";
        return address.matches(addressPattern);
    }
    private boolean isValidSpecialty(String docSpecialties) {
        if (docSpecialties.length() < 1) {
            return false;
        }
        return docSpecialties.matches("^[a-zA-Z,]+( [a-zA-Z,]+)*$");
    }


}

