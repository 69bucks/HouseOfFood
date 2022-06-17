package com.example.houseoffood.Model;

import java.io.Serializable;

public class Users {
    String Username;
    String Email;
    String PhoneNumber;



    String Address;

    public Users(String username, String email, String phoneNumber, String address) {
        Username = username;
        Email = email;
        PhoneNumber = phoneNumber;
        Address = address;
    }



    public Users() {
    }

    public Users(String username, String email, String phoneNumber) {
        this.Username = username;
        this.Email = email;
        this.PhoneNumber = phoneNumber;
    }

    public String getAddress() { return Address; }

    public void setAddress(String address) {Address = address;}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
