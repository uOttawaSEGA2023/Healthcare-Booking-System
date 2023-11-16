package com.example.project10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DoctorAppointmentRequestAdapter extends RecyclerView.Adapter<DoctorAppointmentRequestAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private OnAppointmentActionListener listener;

    // Constructor with listener
    public DoctorAppointmentRequestAdapter(OnAppointmentActionListener listener) {
        this.appointments = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_requests_layout_item, parent, false);
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

    // Interface for handling button actions
    interface OnAppointmentActionListener {
        void onAccept(Appointment appointment);
        void onDeny(Appointment appointment);
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView appDateTextView;
        private final TextView appStartTimeTextView;
        private final TextView appEndTimeTextView;
        private final TextView patientIDTextView;
        private final Button acceptButton;
        private final Button denyButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appDateTextView = itemView.findViewById(R.id.tvAppDate);
            appStartTimeTextView = itemView.findViewById(R.id.tvAppStartTime);
            appEndTimeTextView = itemView.findViewById(R.id.tvAppEndTime);
            patientIDTextView = itemView.findViewById(R.id.tvPatientID);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            denyButton = itemView.findViewById(R.id.denyButton);

            acceptButton.setOnClickListener(v -> listener.onAccept(appointments.get(getAdapterPosition())));
            denyButton.setOnClickListener(v -> listener.onDeny(appointments.get(getAdapterPosition())));
        }

        public void bind(Appointment appointment) {
            appDateTextView.setText(appointment.getAppDate());
            appStartTimeTextView.setText(appointment.getAppStartTime());
            appEndTimeTextView.setText(appointment.getAppEndTime());
            patientIDTextView.setText(appointment.getPatientID());
        }}
}

