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




public class DoctorShiftViewAdapter extends RecyclerView.Adapter<DoctorShiftViewAdapter.ShiftViewHolder> {

    private List<Shift> shifts;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public DoctorShiftViewAdapter() {
        this.shifts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_doctor_shift_layout, parent, false);
        return new ShiftViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftViewHolder holder, int position) {
        Shift shift = shifts.get(position);
        holder.bind(shift);
        holder.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShift(shift);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public void setShifts(List<Shift> newShifts) {
        this.shifts = newShifts;
        notifyDataSetChanged();
    }

    public class ShiftViewHolder extends RecyclerView.ViewHolder {
        private final TextView shiftDateTextView;
        private final TextView shiftStartTimeTextView;
        private final TextView shiftEndTimeTextView;
        public Button cancelButton;
        private FirebaseFirestore firestore;

        public ShiftViewHolder(@NonNull View itemView) {
            super(itemView);
            shiftDateTextView = itemView.findViewById(R.id.tvShiftDate);
            shiftStartTimeTextView = itemView.findViewById(R.id.tvAppStartTime);
            shiftEndTimeTextView = itemView.findViewById(R.id.tvAppEndTime);
            cancelButton = itemView.findViewById(R.id.cancelButtonShift);
            firestore = FirebaseFirestore.getInstance();
        }



        public void bind(Shift shift) {
            shiftDateTextView.setText(shift.getDate());
            shiftStartTimeTextView.setText(shift.getStartTime());
            shiftEndTimeTextView.setText(shift.getEndTime());
        }
    }
    private void deleteShift(Shift shift) {
        String currentUserId = getCurrentUserId();
        String shiftId = shift.getDocumentId();
        if (shiftId != null && !shiftId.isEmpty()) {
            FirebaseFirestore.getInstance().collection("accepted doctors")
                    .document(currentUserId)
                    .collection("upcoming shifts")
                    .document(shiftId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        fetchUpdatedShifts();
                    });
        }
    }

    public void fetchUpdatedShifts() {
        String currentUserId = getCurrentUserId();
        List<Shift> updatedShifts = new ArrayList<>();

        firestore.collection("accepted doctors")
                .document(currentUserId)
                .collection("upcoming shifts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Shift shift = snapshot.toObject(Shift.class);
                            if (shift != null) {
                                updatedShifts.add(shift);
                            }
                        }
                        setShifts(updatedShifts);
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