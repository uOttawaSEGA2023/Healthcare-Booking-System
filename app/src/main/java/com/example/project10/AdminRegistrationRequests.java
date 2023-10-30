package com.example.project10;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminRegistrationRequests extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> userDetailsList;
    private FirebaseFirestore pendingFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_requests);
        listView = (ListView) findViewById(R.id.listView);
        userDetailsList = new ArrayList<>();
        pendingFirestore = FirebaseFirestore.getInstance();
        pendingFirestore.collection("pending users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException error) {
                userDetailsList.clear();
                for(DocumentSnapshot snapshot : documentSnapshots){
                    if(snapshot.exists()) {
                        userDetailsList.add(snapshot.getString("firstName"));
                    }
                }
                adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, userDetailsList);
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        });


    }
}
