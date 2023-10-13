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
    private Button submitButton;

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
                int fieldCount = 0;



                if (first.isEmpty() || last.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty() || phone.isEmpty() || healthCard.isEmpty()) {
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
                    if(healthCard.isEmpty()){
                        fieldCount++;
                    }
                    Toast.makeText(PatientRegistrationActivity.this, "Please fill in " + fieldCount + " missing field(s)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidName(first) || !isValidName(last)) {
                    Toast.makeText(PatientRegistrationActivity.this, "Names should only contain alphabets", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidPassword(password)) {
                    Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid password (6 or more characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidPhone(phone)) {
                    Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidCard(healthCard)) {
                    Toast.makeText(PatientRegistrationActivity.this, "Please enter a valid health card", Toast.LENGTH_SHORT).show();
                    return;
                }


                Patient newPatient = new Patient(first, last, email, password, phone, address, healthCard);

                // TODO: Store the newPatient object in the database or any storage system.

                // Navigate back to MainActivity after registration
                Intent mainIntent = new Intent(PatientRegistrationActivity.this, MainActivity.class);
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
        // Assuming password has 6 or more characters, you can adjust this as per requirements
        return password.length() >= 6;
    }
    private boolean isValidPhone(String phone) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return phone.length() == 10 && phone.matches("[0-9]+");
    }

    private boolean isValidCard(String healthCard) {
        // Assuming phone numbers have at least 10 digits, you can adjust this as per requirements
        return healthCard.length() == 10 && healthCard.matches("[0-9]+");
    }



}

