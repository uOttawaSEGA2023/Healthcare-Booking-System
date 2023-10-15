package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PatientRegistrationActivity extends AppCompatActivity {

    private EditText firstNameInput, lastName, emailAddress, homeAddress, userPassword, phoneNumber, healthCardNumber;
    private Button submitButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_registration);

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
                    Patient newPatient = new Patient(first, last, email, password, phone, address, healthCard);

                    // TODO: Store the newPatient object in the database or any storage system.

                    // Navigate back to MainActivity after registration
                    Intent mainIntent = new Intent(PatientRegistrationActivity.this, MainActivity.class);
                    startActivity(mainIntent);
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


