package com.example.hams;

public class Doctor extends User {
    private int employeeNumber;
    private String[] specialties;

    public Doctor(String firstName, String lastName, String email, String password, int phoneNumber, String address, int employeeNumber, String[] specialties) {
        super(firstName, lastName, email, password, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    // Getters and Setters
    public int getEmployeeNumber(){
        return employeeNumber;
    }

    public void setEmployeeNumber(int newEmployeeNumber){
        employeeNumber = newEmployeeNumber;
    }

    public String[] getSpecialties(){
        return specialties;
    }
    // ...
}