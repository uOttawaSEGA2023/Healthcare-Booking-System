
package com.example.project10;


public class Shift {
    private String date;
    private String endTime;
    private String startTime;

    private String id;
    private String documentId;

    public Shift() {}

    public String getDate() {
        return date;
    }

    public void setDate(String shiftDate) {
        this.date = shiftDate;
    }

    public String getEndTime() {
        return endTime;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}