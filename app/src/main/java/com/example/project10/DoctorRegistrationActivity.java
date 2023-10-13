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
                int fieldCount = 0;

                if (first.isEmpty() || last.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty() || phone.isEmpty() || empNumber.isEmpty() || spec.isEmpty()) {
                    if(first.isEmpty()){
                        fieldCount++;
                    }
                    if(last.isEmpty()){
                        fieldCount++;
                    }
                    if(email.isEmpty()) {
                        fieldCount++;
                    }
                    if(address.isEmpty()){
                        fieldCount++;
                    }
                    if(password.isEmpty()){
                        fieldCount++;
                    }
                    if(phone.isEmpty()){
                        fieldCount++;
                    }
                    if(empNumber.isEmpty()){
                        fieldCount++;
                    }
                    if(spec.isEmpty()){
                        fieldCount++;
                    }
                    Toast.makeText(DoctorRegistrationActivity.this, "Please fill in " + fieldCount + " missing field(s)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidName(first) || !isValidName(last)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Names should only contain alphabets", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidPassword(password)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid password (6 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhone(phone)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid phone number (10 Digits)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmployee(empNumber)) {
                    Toast.makeText(DoctorRegistrationActivity.this, "Please enter a valid employee number (10 Digits)", Toast.LENGTH_SHORT).show();
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

    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+");
    }

    public boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isValidPassword(String password) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return password.length() >= 6;
    }

    private boolean isValidPhone(String phone) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return phone.length() == 10 && phone.matches("[0-9]+");
    }

    private boolean isValidEmployee(String empNumber) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return empNumber.length() == 10 && empNumber.matches("[0-9]+");
    }

    
}
