package com.example.project10;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private Context context;
    private ArrayList<String> users; // List of user details
    private ArrayList<String> userIds; // List of user IDs
    private FirebaseFirestore firestore;

    public UserAdapter(Context context, ArrayList<String> users, ArrayList<String> userIds) {
        this.context = context;
        this.users = users;
        this.userIds = userIds;
        this.firestore = FirebaseFirestore.getInstance(); // Initialize Firestore
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        String user = users.get(position);
        String userId = userIds.get(position); // Get the corresponding userId
        holder.setDetails(user, userId);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private TextView textName, textRole;
        private Button acceptButton, denyButton;

        UserHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textRole = itemView.findViewById(R.id.textRole);

            acceptButton = itemView.findViewById(R.id.acceptButton);
            denyButton = itemView.findViewById(R.id.denyButton);
        }

        void setDetails(String user, String userId) {
            String[] parts = user.split(" \\| ");
            if (parts.length > 0) {
                textName.setText(parts[0]);
            }
            if (parts.length > 1) {
                textRole.setText(parts[1]);
            }


            acceptButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    moveUserToCollection(userId, "accepted users");
                }
            });

            denyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    moveUserToCollection(userId, "rejected users");
                }
            });
        }
    }

    private void moveUserToCollection(String userId, String collectionName) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("pending users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if (userData != null) {
                        firestore.collection(collectionName).document(userId).set(userData)
                                .addOnSuccessListener(aVoid ->
                                        firestore.collection("pending users").document(userId).delete()
                                );
                    }
                });
    }
}



