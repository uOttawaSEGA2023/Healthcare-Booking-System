package com.example.project10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class DoctorAppointmentViewAdapter extends RecyclerView.Adapter<DoctorAppointmentViewAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public DoctorAppointmentViewAdapter() {
        this.appointments = new ArrayList<>();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_appointment_layout, parent, false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment);
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAppointment(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(List<Appointment> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView appDateTextView;
        private final TextView appStartTimeTextView;
        private final TextView appEndTimeTextView;
        private final TextView patientNameTextView;
        private final TextView patientEmailTextView;
        private final TextView patientPhoneNumberTextView;
        private final TextView patientHealthCardNumberTextView;
        public Button cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appDateTextView = itemView.findViewById(R.id.tvAppDate);
            appStartTimeTextView = itemView.findViewById(R.id.tvAppStartTime);
            appEndTimeTextView = itemView.findViewById(R.id.tvAppEndTime);
            patientNameTextView = itemView.findViewById(R.id.tvPatientName);
            patientEmailTextView = itemView.findViewById(R.id.tvPatientEmail);
            patientPhoneNumberTextView = itemView.findViewById(R.id.tvPatientPhoneNumber);
            patientHealthCardNumberTextView = itemView.findViewById(R.id.tvPatientHealthCardNumber);
            cancelButton = itemView.findViewById(R.id.cancelButtonAppointment);
            firestore = FirebaseFirestore.getInstance();
        }

        public void bind(Appointment appointment) {
            appDateTextView.setText(appointment.getAppDate());
            appStartTimeTextView.setText(appointment.getAppStartTime());
            appEndTimeTextView.setText(appointment.getAppEndTime());

            String patientID = appointment.getPatientID();
            firestore.collection("accepted patients")
                    .document(patientID)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Patient patient = documentSnapshot.toObject(Patient.class);
                            patientNameTextView.setText(patient.getFirstName() + " " + patient.getLastName());
                            patientEmailTextView.setText(patient.getEmail());
                            patientPhoneNumberTextView.setText(patient.getPhone());
                            patientHealthCardNumberTextView.setText(patient.getHealthCardNumber());
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error appropriately
                    });
        }
    }

    private void deleteAppointment(Appointment appointment) {
        String currentUserId = getCurrentUserId();
        String appointmentId = appointment.getDocumentId();
        if (appointmentId != null && !appointmentId.isEmpty()) {
            FirebaseFirestore.getInstance().collection("accepted doctors")
                    .document(currentUserId)
                    .collection("upcoming appointments")
                    .document(appointmentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        fetchUpdatedAppointments();
                    });
        }
    }

    public void fetchUpdatedAppointments() {
        String currentUserId = getCurrentUserId();
        List<Appointment> updatedAppointments = new ArrayList<>();

        firestore.collection("accepted doctors")
                .document(currentUserId)
                .collection("upcoming appointments")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Appointment appointment = snapshot.toObject(Appointment.class);
                            if (appointment != null) {
                                updatedAppointments.add(appointment);
                            }
                        }
                        setAppointments(updatedAppointments);
                    }
                })
                .addOnFailureListener(e -> {
                });
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }
}
