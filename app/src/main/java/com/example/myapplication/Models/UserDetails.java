package com.example.myapplication.Models;

public class UserDetails {
    private String uid;
    private String username;
    private String email;
    private String password;
    private String nim;

    public UserDetails() {
        // Default constructor required for calls to DataSnapshot.getValue(UserDetails.class)
    }

    public UserDetails(String uid, String username, String email, String password, String nim) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.nim = nim;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNim() {
        return nim;
    }
}