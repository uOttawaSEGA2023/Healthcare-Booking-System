package com.example.project10;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.widget.Toast;

public class PatientAppointmentSearchAdapter extends RecyclerView.Adapter<PatientAppointmentSearchAdapter.DoctorViewHolder> {

    private List<Doctor> doctors;

    public PatientAppointmentSearchAdapter() {
        this.doctors = new ArrayList<>();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_doctor_layout, parent, false);
        return new DoctorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bind(doctor);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public void setDoctors(List<Doctor> newDoctors) {
        this.doctors = newDoctors;
        notifyDataSetChanged();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        private final TextView doctorNameTextView;
        private final TextView doctorSpecializationTextView;
        private final Button selectButton;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.tvDoctorName);
            doctorSpecializationTextView = itemView.findViewById(R.id.tvDoctorSpecialization);
            selectButton = itemView.findViewById(R.id.selectDoctorButton);

            selectButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Doctor selectedDoctor = doctors.get(position);
                    String doctorDocumentID = selectedDoctor.getDocumentId();
                    if (doctorDocumentID != null) {
                        Intent intent = new Intent(itemView.getContext(), PatientRequestAppointmentWithSpecificDoctor.class);
                        intent.putExtra("DOCTOR_DOCUMENT_ID", doctorDocumentID);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Doctor Document ID is null", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void bind(Doctor doctor) {
            doctorNameTextView.setText(doctor.getFirstName() + " " + doctor.getLastName());
            doctorSpecializationTextView.setText(doctor.getSpecialties());
        }
    }
}

