package com.example.project10;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    //User Adaptor
    private Context context;
    private ArrayList<String> users;

    private FirebaseFirestore newFirestore;

    public UserAdapter(Context context, ArrayList<String> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.user_layout_item,
                parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position){
        String user = users.get(position);
        holder.setDetails(user);
    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    //User Holder
    class UserHolder extends RecyclerView.ViewHolder{
        private TextView textName, textRole;
        private Button acceptButton, denyButton;

        public UserHolder(@NonNull View itemView){
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textRole = itemView.findViewById(R.id.textRole);

            acceptButton = itemView.findViewById(R.id.acceptButton);
            denyButton = itemView.findViewById(R.id.denyButton);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    newFirestore = FirebaseFirestore.getInstance();
//
//                    Map<String, Object> user = new HashMap<>();
//                    user.put("name", textName);
//                    newFirestore.collection("users").document().set(user);
                }
            });

            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }



        void setDetails(String user){
            String[] details = user.split(", ");
            textName.setText(details[0]);
            textRole.setText(details[1]);
        }
    }
}
