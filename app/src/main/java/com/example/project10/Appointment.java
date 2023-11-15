package com.example.project10;


public class Appointment {
    private String appDate;
    private String appEndTime;
    private String appStartTime;
    private String patientID;

    private String id;
    private String documentId;

    // Make sure to have an empty constructor for Firestore's automatic data mapping
    public Appointment() {}

    // Getters and setters for each field
    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getAppEndTime() {
        return appEndTime;
    }

    public void setAppEndTime(String appEndTime) {
        this.appEndTime = appEndTime;
    }

    public String getAppStartTime() {
        return appStartTime;
    }

    public void setAppStartTime(String appStartTime) {
        this.appStartTime = appStartTime;
    }

    public String getPatientID() {
        return patientID;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

