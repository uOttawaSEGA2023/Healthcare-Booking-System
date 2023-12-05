package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;


public class MainActivity extends AppCompatActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button signInButton;
    private Button registerButton;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailSignIn);
        passwordEditText = findViewById(R.id.passwordSignIn);
        signInButton = findViewById(R.id.signIn);
        registerButton = findViewById(R.id.register);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent roleIntent = new Intent(MainActivity.this, RoleActivity.class);
                startActivity(roleIntent);
                finish();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(emailEditText.getText()).equals("") ||String.valueOf(passwordEditText.getText()).equals("")){
                    Toast.makeText(MainActivity.this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = String.valueOf(emailEditText.getText());
                String password = String.valueOf(passwordEditText.getText());

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser currentUser = mAuth.getCurrentUser();
                                    if (currentUser != null) {
                                        String userId = currentUser.getUid();
                                        fstore.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                String role = documentSnapshot.getString("role");
                                                Intent intent;
                                                if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
                                                    intent = new Intent(MainActivity.this, AdminWelcomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else if ("doctor".equals(role)) {
                                                    intent = new Intent(MainActivity.this, DoctorWelcomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else if ("patient".equals(role)) {
                                                    intent = new Intent(MainActivity.this, PatientWelcomeActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    // Role is not admin, doctor, or patient
                                                    Toast.makeText(MainActivity.this, "Invalid role or credentials. Please try again.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Login Unsuccessful.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }}
