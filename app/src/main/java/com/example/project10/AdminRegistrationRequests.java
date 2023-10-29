package com.example.project10;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRegistrationRequests extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> userDetailsList;
    private FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_requests);

        listView = findViewById(R.id.listView);
        userDetailsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userDetailsList);
        listView.setAdapter(adapter);

        fstore = FirebaseFirestore.getInstance();
        loadPendingUserIds();
    }

    private void loadPendingUserIds() {
        fstore.collection("ID Status")
                .whereEqualTo("status", "pending")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String userId = documentSnapshot.getId();
                        loadUserData(userId);
                    }

                });
    }

    private void loadUserData(String userId) {
        fstore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve user details and format them
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String phone = documentSnapshot.getString("phone");

                        // Here, you can add more fields as needed
                        String displayInfo = "UserID: " + userId + ", Name: " + name + ", Email: " + email+ ", Phone: " + phone;
                        userDetailsList.add(displayInfo);
                        adapter.notifyDataSetChanged();
                    }

                });
    }
}
