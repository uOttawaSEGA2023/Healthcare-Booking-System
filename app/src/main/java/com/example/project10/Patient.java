//Patient class derived from User
package com.example.project10;

public class Patient extends User {
    private String healthCardNumber;



    public Patient(String firstName, String lastName, String email, String password, String phone, String address, String healthCardNumber) {
        super(firstName, lastName, email, password, phone, address);
        this.healthCardNumber = healthCardNumber;


    }

    public Patient() {
    }



    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }
}