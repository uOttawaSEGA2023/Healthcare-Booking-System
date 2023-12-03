package com.example.project10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PatientUpcomingAppointmentsAdapter extends RecyclerView.Adapter<PatientUpcomingAppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public PatientUpcomingAppointmentsAdapter() {
        this.appointments = new ArrayList<>();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_patient_upcoming_appointments_layout, parent, false);
        return new AppointmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment);
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
        private final TextView doctorNameTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appDateTextView = itemView.findViewById(R.id.tvAppDate);
            appStartTimeTextView = itemView.findViewById(R.id.tvAppStartTime);
            appEndTimeTextView = itemView.findViewById(R.id.tvAppEndTime);
            doctorNameTextView = itemView.findViewById(R.id.tvDoctorName);
        }

        public void bind(Appointment appointment) {
            appDateTextView.setText(appointment.getAppDate());
            appStartTimeTextView.setText(appointment.getAppStartTime());
            appEndTimeTextView.setText(appointment.getAppEndTime());
            doctorNameTextView.setText(appointment.getDoctorName());
        }
    }



    private String getCurrentUserId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null ? currentUser.getUid() : "";
    }
}


