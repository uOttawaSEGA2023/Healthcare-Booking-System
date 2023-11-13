package com.example.project10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class UserDeniedAdapter extends RecyclerView.Adapter<UserDeniedAdapter.UserDeniedHolder> {
    private Context context;
    private ArrayList<String> users; // List of user details
    private ArrayList<String> userIds; // List of user IDs
    private FirebaseFirestore firestore;

    public UserDeniedAdapter(Context context, ArrayList<String> users, ArrayList<String> userIds) {
        this.context = context;
        this.users = users;
        this.userIds = userIds;
        this.firestore = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @NonNull
    @Override
    public UserDeniedAdapter.UserDeniedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_denied_layout_item, parent, false);
        return new UserDeniedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDeniedAdapter.UserDeniedHolder holder, int position) {
        String user = users.get(position);
        String userId = userIds.get(position); // Get the corresponding userId
        holder.setDetails(user, userId);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserDeniedHolder extends RecyclerView.ViewHolder {
        private TextView textName, textRole, textEHNumber, textEmail, textPhone, textSpec;
        private Button acceptButton;

        UserDeniedHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName1);
            textRole = itemView.findViewById(R.id.textRole1);
            textEHNumber = itemView.findViewById(R.id.textEHNumber1);
            textEmail = itemView.findViewById(R.id.textEmail1);
            textPhone = itemView.findViewById(R.id.textPhone1);
            textSpec = itemView.findViewById(R.id.textSpec1);

            acceptButton = itemView.findViewById(R.id.acceptButton1);
        }

        void setDetails(String user, String userId) {
            String[] parts = user.split(", ");
            textName.setText(parts[0]);
            textRole.setText(parts[1]);
            textEmail.setText(parts[2]);
            textPhone.setText("Phone #: " + parts[3]);

            if(parts.length == 5){
                textEHNumber.setText("Health Card: " + parts[4]);
                textSpec.setText("No specialties");
            } else {
                textEHNumber.setText("Employee #" + parts[4]);
                textSpec.setText("Specialties: " + parts[5]);
            }

            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    moveUserToCollection(userId);
                }
            });
        }
    }

    private void moveUserToCollection(String userId) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("rejected users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData != null) {
                        String role = (String) userData.get("role");
                        String targetCollection;

                        // Determine the target collection based on the role
                        if ("doctor".equals(role)) {
                            targetCollection = "accepted doctors";
                        } else if ("patient".equals(role)) {
                            targetCollection = "accepted patients";
                        } else {
                            targetCollection = "accepted users"; // or any other logic as per your requirement
                        }

                        // Move the user to the determined collection
                        firestore.collection(targetCollection).document(userId).set(userData)
                                .addOnSuccessListener(aVoid ->
                                        firestore.collection("rejected users").document(userId).delete()
                                );
                    }
                });
    }
}





