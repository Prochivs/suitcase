package com.example.suit_case_application.model;

public class User {
    private String id;
    private String fullNames;
    private String emailAddress;
    private String username;

    public User() {
    }

    public User(String id, String fullNames, String emailAddress, String username) {
        this.id = id;
        this.fullNames = fullNames;
        this.emailAddress = emailAddress;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullNames() {
        return fullNames;
    }

    public void setFullNames(String fullNames) {
        this.fullNames = fullNames;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
