package com.example.project10;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.TextView;

import org.junit.Test;

public class MainActivityTest {


    @Test
    public void adminLogInIsCorrect() {
        MainActivity mainActivity = new MainActivity();

        mainActivity.emailEditText.setText("admin@gmail.com");
        mainActivity.passwordEditText.setText("admin123");

        mainActivity.signInButton.callOnClick();

        TextView welcomeTextView = mainActivity.findViewById(R.id.welcomePatient);
        assertNotNull("Welcome TextView should not be null", welcomeTextView);
        assertEquals("Welcome Admin!", welcomeTextView.getText().toString());
    }

    @Test
    public void isValidUser(){

        MainActivity mainActivity = new MainActivity();
        String email = mainActivity.emailEditText.getText().toString();
        String password = mainActivity.passwordEditText.getText().toString();

        mainActivity.emailEditText.setText(email);
        mainActivity.passwordEditText.setText(password);

        mainActivity.signInButton.callOnClick();

        TextView welcomeTextView = mainActivity.findViewById(R.id.welcomePatient);
        assertEquals("Welcome", welcomeTextView.getText().toString());

    }
}