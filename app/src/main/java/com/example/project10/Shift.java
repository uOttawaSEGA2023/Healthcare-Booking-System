
package com.example.project10;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
    public List<Shift> splitInto30MinIntervals() {
        List<Shift> intervals = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            Date start = sdf.parse(this.getStartTime());
            Date end = sdf.parse(this.getEndTime());
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(start);
            while (calendar.getTime().before(end)) {
                Shift interval = new Shift(); // Assuming you have a constructor to set date, startTime, endTime
                interval.setDate(this.getDate());
                interval.setStartTime(sdf.format(calendar.getTime()));

                calendar.add(Calendar.MINUTE, 30);
                interval.setEndTime(sdf.format(calendar.getTime()));

                intervals.add(interval);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return intervals;
    }

}