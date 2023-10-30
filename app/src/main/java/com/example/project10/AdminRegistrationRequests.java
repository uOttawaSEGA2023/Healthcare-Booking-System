package com.example.project10;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRegistrationRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<String> userDetailsList;

    private Button backButton;

    private UserAdapter adapter;
    private FirebaseFirestore pendingFirestore;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_requests);
        recyclerView = (RecyclerView) findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userDetailsList = new ArrayList<>();
        pendingFirestore = FirebaseFirestore.getInstance();

        pendingFirestore.collection("pending users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException error) {
                userDetailsList.clear();
                adapter = new UserAdapter(getApplicationContext(), userDetailsList);
                for(DocumentSnapshot snapshot : documentSnapshots){
                    if(snapshot.exists()) {
                        String firstName = snapshot.getString("firstName");
                        String lastName = snapshot.getString("lastName");
                        String role = snapshot.getString("role");
                        String details = firstName + " " + lastName + ", " + role;
                        userDetailsList.add(details);
                    }
                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
            }
        });

        backButton = findViewById(R.id.backRequestButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminRegistrationRequests.this, AdminWelcomeActivity.class);
                startActivity(intent);
            }
        });


    }
}
