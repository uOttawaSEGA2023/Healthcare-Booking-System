package com.example.project10;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.HashMap;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class PatientRequestAppointmentWithSpecificDoctorAdapter extends RecyclerView.Adapter<PatientRequestAppointmentWithSpecificDoctorAdapter.TimeSlotViewHolder> {

    private List<Shift> timeSlots;
    private String doctorDocumentID;
    private String currentUserId;

    public PatientRequestAppointmentWithSpecificDoctorAdapter(String doctorDocumentID, String currentUserId) {
        this.timeSlots = new ArrayList<>();
        this.doctorDocumentID = doctorDocumentID;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_item_layout, parent, false);
        return new TimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        Shift timeSlot = timeSlots.get(position);
        holder.bind(timeSlot, doctorDocumentID, currentUserId);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public void setShiftsAsAppointments(List<Shift> shifts) {
        this.timeSlots.clear();
        for (Shift shift : shifts) {
            this.timeSlots.addAll(shift.splitInto30MinIntervals());
        }
        notifyDataSetChanged();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTextView;
        private final TextView timeRangeTextView;
        private final Button selectButton;
        private FirebaseFirestore fstore;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeRangeTextView = itemView.findViewById(R.id.timeRangeTextView);
            selectButton = itemView.findViewById(R.id.selectAppButton);
            fstore = FirebaseFirestore.getInstance();
        }

        public void bind(Shift timeSlot, String doctorDocumentID, String currentUserId) {
            dateTextView.setText(timeSlot.getDate());
            timeRangeTextView.setText(timeSlot.getStartTime() + " - " + timeSlot.getEndTime());

            selectButton.setOnClickListener(v -> {
                fstore.collection("accepted doctors").document(doctorDocumentID).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String firstName = documentSnapshot.getString("firstName");
                                String lastName = documentSnapshot.getString("lastName");
                                String doctorName = firstName + " " + lastName;

                                Map<String, Object> appointment = new HashMap<>();
                                appointment.put("appDate", timeSlot.getDate());
                                appointment.put("appStartTime", timeSlot.getStartTime());
                                appointment.put("appEndTime", timeSlot.getEndTime());
                                appointment.put("patientID", currentUserId);
                                appointment.put("doctorName", doctorName);

                                addAppointmentToFirestore(appointment, doctorDocumentID, currentUserId);
                            }
                        })
                        .addOnFailureListener(e -> {
                        });
            });
        }

        private void addAppointmentToFirestore(Map<String, Object> appointment, String doctorDocumentID, String currentUserId) {
            fstore.collection("accepted doctors").document(doctorDocumentID)
                    .collection("appointment requests").add(appointment);

            fstore.collection("accepted patients").document(currentUserId)
                    .collection("upcoming appointments").add(appointment);

            returnToPatientRequestAppointmentScreen();
        }
        private void returnToPatientRequestAppointmentScreen() {
            Intent intent = new Intent(itemView.getContext(), PatientRequestAppointment.class);
            itemView.getContext().startActivity(intent);
        }
    }
}
