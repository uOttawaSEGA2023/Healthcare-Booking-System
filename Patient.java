package com.example.hams;


public class Patient extends User {
    private int healthCardNumber;

    public Patient(String firstName, String lastName, String email, String password, int phoneNumber, String address, int healthCardNumber) {
        super(firstName, lastName, email, password, phoneNumber, address);
        this.healthCardNumber = healthCardNumber;
    }

    // Getters and Setters
    public int getHealthCardNumber(){
        return healthCardNumber;
    }

    public void setHealthCardNumber(int newNumber){
        healthCardNumber = newNumber;
    }
    // ...
}