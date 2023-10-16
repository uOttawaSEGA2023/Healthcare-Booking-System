//Patient class derived from User
package com.example.project10;

public class Patient extends User {
    private String healthCardNumber;

    public Patient(String firstName, String lastName, String emailAddress, String password, String phoneNumber, String address, String healthCardNumber) {
        super(firstName, lastName, emailAddress, password, phoneNumber, address);
        this.healthCardNumber = healthCardNumber;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }
}