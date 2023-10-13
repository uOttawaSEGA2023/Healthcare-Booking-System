package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorRegistrationActivity extends AppCompatActivity {

    private EditText firstNameInput, lastName, emailAddress, homeAddress, userPassword, phoneNumber, employeeNumber, specialties;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_registration);

        // Initialize UI components
        firstNameInput = findViewById(R.id.firstNameInput);
        lastName = findViewById(R.id.lastName);
        emailAddress = findViewById(R.id.emailAddress);
        homeAddress = findViewById(R.id.homeAddress);
        userPassword = findViewById(R.id.userPassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        employeeNumber = findViewById(R.id.employeeNumber);
        specialties = findViewById(R.id.Specialties);
        submitButton = findViewById(R.id.submitButton);

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
                String empNumber = employeeNumber.getText().toString().trim();
                String spec = specialties.getText().toString().trim();

                if (!isValidEmail(email)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhone(phone)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty() || first.isEmpty() || last.isEmpty() || address.isEmpty() || empNumber.isEmpty() || spec.isEmpty()) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidName(first) || !isValidName(last)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Names should only contain alphabets", Toast.LENGTH_SHORT).show();
                    return;
                }

                Doctor newDoctor = new Doctor(first, last, email, password, phone, address, empNumber, spec);

                // TODO: Store the newDoctor object in the database or any storage system.

                // Navigate back to MainActivity after registration
                Intent mainIntent = new Intent(DoctorRegistrationActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    public boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isValidPhone(String phone) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return phone.length() >= 10 && phone.matches("[0-9]+");
    }

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }
}
