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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private Button registerButton;
    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            startActivity(welcomeIntent);
            finish();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
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
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = String.valueOf(emailEditText.getText());
                final String password = String.valueOf(passwordEditText.getText());

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    if ("admin@gmail.com".equals(email) && "admin123".equals(password)) {
                                        // For admin credentials
                                        Intent adminIntent = new Intent(MainActivity.this, AdminWelcomeActivity.class);
                                        startActivity(adminIntent);
                                        finish();
                                    } else {
                                        // For other users
                                        Toast.makeText(MainActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                                        Intent welcomeIntent = new Intent(MainActivity.this, WelcomeActivity.class);
                                        startActivity(welcomeIntent);
                                        finish();
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "Login Unsuccessful.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }}
