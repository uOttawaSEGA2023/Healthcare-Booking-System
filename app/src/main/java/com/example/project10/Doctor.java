//Doctor class derived from User
package com.example.project10;

public class Doctor extends User {
    private String employeeNumber;
    private String specialties;
    private String documentId;


    public Doctor(String firstName, String lastName, String emailAddress, String password, String phoneNumber, String address, String employeeNumber, String specialties, String documentId) {
        super(firstName, lastName, emailAddress, password, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        this.documentId = documentId;
    }

    public Doctor() {
        // Required for Firebase deserialization
    }
    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
