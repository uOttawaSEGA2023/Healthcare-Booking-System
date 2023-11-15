package com.example.project10;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TimePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Date;

public class DoctorCreateAppointmentActivity extends AppCompatActivity {

    private EditText editTextStartDate, editTextStartTime, editTextEndTime;
    private Button buttonCreateAppointment, logOutButton, backButton;
    private TextView headerTitle;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_create_appointment);

        editTextStartDate = findViewById(R.id.doctor_appointment_date);
        editTextStartTime = findViewById(R.id.doctor_appointment_start);
        editTextEndTime = findViewById(R.id.doctor_appointment_end);
        buttonCreateAppointment = findViewById(R.id.doctor_button_create_appointment);
        headerTitle = findViewById(R.id.headerTitle2);

        logOutButton = findViewById(R.id.doctorLogOut);
        backButton = findViewById(R.id.doctorCreateAppBackButton);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(true);
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker(false);
            }
        });

        buttonCreateAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAppointment();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainIntent = new Intent(DoctorCreateAppointmentActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(DoctorCreateAppointmentActivity.this, DoctorWelcomeActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                String formattedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
                editTextStartDate.setText(formattedDate);
            }
        }, year, month, day);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void openTimePicker(final boolean isStartTime) {
        Calendar calendar = Calendar.getInstance();
        final int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        final int currentMinute = calendar.get(Calendar.MINUTE);
        final int roundedMinute = roundToNearestHalfHour(currentMinute);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                selectedMinute = (selectedMinute < 30) ? 0 : 30;
                if (isToday() && (selectedHour < currentHour || (selectedHour == currentHour && selectedMinute < roundedMinute))) {
                    Toast.makeText(DoctorCreateAppointmentActivity.this, "Please select a future time", Toast.LENGTH_SHORT).show();
                    return;
                }
                String formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute);
                if (isStartTime) {
                    editTextStartTime.setText(formattedTime);
                } else {
                    editTextEndTime.setText(formattedTime);
                }
            }
        }, currentHour, roundedMinute, true);

        timePickerDialog.show();
    }

    private int roundToNearestHalfHour(int minute) {
        return (minute < 15 || minute >= 45) ? 0 : 30;
    }

    private boolean isToday() {
        Calendar calendar = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        try {
            selectedDate.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(editTextStartDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return calendar.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    private void createAppointment() {
        final String startDate = editTextStartDate.getText().toString();
        final String startTime = editTextStartTime.getText().toString();
        final String endTime = editTextEndTime.getText().toString();

        if (startDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String doctorId = mAuth.getCurrentUser().getUid();
        fstore.collection("accepted doctors").document(doctorId)
                .collection("appointments").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                            String existingStartDate = snapshot.getString("date");
                            String existingStartTime = snapshot.getString("startTime");
                            String existingEndTime = snapshot.getString("endTime");

                            if (isOverlapping(startDate + " " + startTime, startDate + " " + endTime, existingStartDate + " " + existingStartTime, existingStartDate + " " + existingEndTime)) {
                                Toast.makeText(DoctorCreateAppointmentActivity.this, "Appointment overlaps with an existing one", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        createNewAppointment(startDate, startTime, endTime);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(DoctorCreateAppointmentActivity.this, "Error checking appointments", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static boolean isOverlapping(String start1, String end1, String start2, String end2) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date dateStart1 = format.parse(start1);
            Date dateEnd1 = format.parse(end1);
            Date dateStart2 = format.parse(start2);
            Date dateEnd2 = format.parse(end2);

            return dateStart1.before(dateEnd2) && dateStart2.before(dateEnd1);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createNewAppointment(String startDate, String startTime, String endTime) {
        Map<String, Object> shift = new HashMap<>();
        shift.put("date", startDate);
        shift.put("startTime", startTime);
        shift.put("endTime", endTime);
        shift.put("Appointment Request", "none");

        String doctorId = mAuth.getCurrentUser().getUid();

        fstore.collection("accepted doctors").document(doctorId)
                .collection("upcoming shifts").add(shift)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(DoctorCreateAppointmentActivity.this, "Shift Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(DoctorCreateAppointmentActivity.this, DoctorWelcomeActivity.class);
                        startActivity(mainIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(DoctorCreateAppointmentActivity.this, "Error Creating Shift", Toast.LENGTH_SHORT).show();
                    }
                });

        backButton = findViewById(R.id.doctorCreateAppBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent for navigating back or just finish the current activity
                finish(); // This will close the current activity and return to the previous one
            }
        });
        }
    }


