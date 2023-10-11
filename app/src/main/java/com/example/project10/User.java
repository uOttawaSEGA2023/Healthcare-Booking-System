package com.example.project10;

public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected int phoneNumber;
    protected String address;

    public User(String firstName, String lastName, String email, String password, int phoneNumber, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String newFirstName){
        firstName = newFirstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String newLastName){
        lastName = newLastName;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String newEmail){
        email = newEmail;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String newPassword){
        password = newPassword;
    }

    public int getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(int newPhoneNumber){
        phoneNumber = newPhoneNumber;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String newAddress){
        address = newAddress;
    }

    // ...
}